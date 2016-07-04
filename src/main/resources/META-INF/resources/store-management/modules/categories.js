/*
 * Copyright (c) 2013-2015, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
define([
    'require',
    'module',
    '{angular}/angular',
    '{w20-core}/modules/ui'
], function (require, module, angular) {
    'use strict';

    var config = module && module.config() || {},
        seedStoreCategoryManagement = angular.module('CategoryManagement', ['ui.bootstrap']);
    seedStoreCategoryManagement.value('storedCategory', {});
    seedStoreCategoryManagement.value('editedCategory', {});

    seedStoreCategoryManagement.factory('CategoryService', ['$resource', function ($resource) {
        var Category = $resource(require.toUrl(config.apiUrl + 'categories/:categoryId'), {categoryId: '@id'}, {
            'update': {method: 'PUT'}
        });

        var ProductsByCategory = $resource(require.toUrl(config.apiUrl + 'categories/:categoryId/products'));

        return {
            allCategories: function (success, error) {
                return Category.query({}, success, error);
            },

            allPaginatedCategories: function (pageIndex, pageSize, success, error) {
                return Category.query({pageIndex: pageIndex - 1, pageSize: pageSize}, success, error);
            },

            paginatedProductsByCategrory: function (categoryId, pageIndex, pageSize, success, error) {
                return ProductsByCategory.query({
                    categoryId: categoryId,
                    pageIndex: pageIndex - 1,
                    pageSize: pageSize
                }, success, error);
            },

            searchPaginatedCategories: function (searchString, pageIndex, nbCat, success, error) {
                return Category.query({
                    searchString: searchString,
                    pageIndex: pageIndex - 1,
                    pageSize: nbCat
                }, success, error);
            },

            category: function (id) {
                return Category.get({categoryId: id});
            },

            deleteCategory: function (category, success, error) {
                category.$delete(success, error);
            },

            updateCategory: function (category, success, error) {
                category.$update(success, error);
            },

            addCategory: function (category, success, error) {
                var newCategory = new Category();

                newCategory.name = category.name;
                newCategory.urlImg = category.urlImg;

                newCategory.$save(success, error);
            }
        };
    }]);

    seedStoreCategoryManagement.controller('ModalCategoryController', ['$scope', '$uibModalInstance', 'category', 'modalTitle', function ($scope, $uibModalInstance, category, modalTitle) {
        $scope.modalTitle = modalTitle;
        $scope.category = category;
        $scope.ok = function () {
            $uibModalInstance.close($scope.category);
        };
        $scope.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };
    }]);

    seedStoreCategoryManagement.controller('CategoryManagementController', ['$scope', '$uibModal', '$timeout', 'CategoryService', function ($scope, $uibModal, $timeout, categoryService) {

        function modal(title, template, controller, category, callback) {
            var modalInstance = $uibModal.open({
                templateUrl: template,
                controller: controller,
                resolve: {
                    category: function () { return category; },
                    modalTitle: function() { return title; }
                }
            });
            modalInstance.result.then(function(data) { callback(data); });
        }

        function getCategoriesSuccess(data) {
            $scope.paginatedCategories = data;
            $scope.pagingCategories.totalServerItems = $scope.paginatedCategories.$viewInfo.resultSize;
            if ($scope.paginatedCategories.length) {
                if ($scope.activeCategory !== $scope.paginatedCategories[0]) {
                    $scope.setActiveCategory($scope.paginatedCategories[0]);
                }
                getProductsByCategory($scope.activeCategory.id);
            }
        }

        function getCategoriesError(err) {
            throw new Error('Could not get categories ' + err.message);
        }

        function getCategories() {
            categoryService.allPaginatedCategories($scope.pagingCategories.currentPage, $scope.pagingCategories.pageSize, getCategoriesSuccess, getCategoriesError);
        }

        function getProductsByCategory(categoryId) {
            categoryService.paginatedProductsByCategrory(categoryId, $scope.pagingProducts.currentPage, $scope.pagingProducts.pageSize,
                function (data) {
                    $scope.paginatedProducts = data;
                    $scope.pagingProducts.totalServerItems = $scope.paginatedProducts.$viewInfo.resultSize;
                },
                function (error) {
                    throw new Error('Could not get associated products ' + error.message);
                });
        }

        function searchCategories(searchString) {
            if (searchString) {
                categoryService.searchPaginatedCategories(searchString, $scope.pagingCategories.currentPage, $scope.pagingCategories.pageSize, getCategoriesSuccess, getCategoriesError);
            } else {
                getCategories();
            }
        }

        $scope.pagingCategories = {
            pageSize: 6,
            currentPage: 1,
            totalServerItems: 0
        };

        $scope.pagingProducts = {
            pageSize: 10,
            currentPage: 1,
            totalServerItems: 0
        };

        $scope.pageCategoryChanged = function() {
            getCategories();
        };

        $scope.pageProductChanged = function() {
            getProductsByCategory($scope.activeCategory.id);
        };

        $scope.setActiveCategory = function(category) {
            $scope.activeCategory = category;
            getProductsByCategory($scope.activeCategory.id, $scope.pagingProducts);
        };

        $scope.createNewCategory = function() {
            modal('Add a category', 'modalCategory.html', 'ModalCategoryController', {}, function(newCategory) {
                categoryService.addCategory(newCategory,
                    function() {
                        getCategories();
                    },
                    function(err) {
                        throw new Error('Could not add new category ' + err.message);
                    });
            });
        };

        $scope.editCategory = function(category) {
            modal('Edit category', 'modalCategory.html', 'ModalCategoryController', category, function (category) {
                categoryService.updateCategory(category,
                    function () {
                        getCategories();
                    },
                    function (err) {
                        throw new Error('Could not update category ' + err.message);
                    });
            });
        };

        $scope.deleteCategory = function(category) {
            modal('Delete a category', 'modalConfirmCategory.html', 'ModalCategoryController', category, function (category) {
                categoryService.deleteCategory(category,
                    function() {
                        getCategories();
                    },
                    function(err) {
                        throw new Error('Could not delete category ' + err.message);
                    });
            });
        };

        $scope.$watch('searchedCategory', function(newSearch, oldSearch) {
            if (newSearch !== oldSearch) {
                searchCategories(newSearch);
            }
        });

        getCategories();
    }]);

    return {
        angularModules: ['CategoryManagement']
    };
});
