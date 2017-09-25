/*
 * Copyright (c) 2013-2016, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
define([
    'require',
    'module',
    '{angular}/angular',
    '{angular-sanitize}/angular-sanitize',
    '{store-management}/modules/categories',
    '{w20-core}/modules/ui'
], function (require, module, angular) {
    'use strict';

    var seedStoreProductManagement = angular.module('ProductManagement', ['ngSanitize', 'CategoryManagement', 'ui.bootstrap']),
        config = module && module.config() || {};

    seedStoreProductManagement.factory('ProductService', ['$resource', function ($resource) {
        var Product = $resource(require.toUrl(config.apiUrl + 'products/:productId'), {productId: '@id'}, {
            'update': {method: 'PUT'}
        });

        return {
            allProducts: function (success, error) {
                return Product.query({}, success, error);
            },

            allPaginatedProducts: function (pageIndex, pageSize, success, error) {
                return Product.query({pageIndex: pageIndex, pageSize: pageSize}, success, error);
            },

            searchPaginatedProducts: function (categoryId, searchString, pageIndex, pageSize, success, error) {
                return Product.query({
                    categoryId: categoryId,
                    searchString: searchString,
                    pageIndex: pageIndex,
                    pageSize: pageSize
                }, success, error);
            },

            product: function (id, success, error) {
                return Product.get({productId: id}, success, error);
            },

            deleteProduct: function (product, success, error) {
                product.$delete(success, error);
            },

            updateProduct: function (product, success, error) {
                product.$update(success, error);
            },

            addProduct: function (product, success, error) {
                var newProduct = new Product();

                newProduct.designation = product.designation;
                newProduct.summary = product.summary;
                newProduct.details = product.details;
                newProduct.picture = product.picture;
                newProduct.price = product.price;
                newProduct.categoryId = product.categoryId;

                newProduct.$save(success, error);
            }
        };
    }]);

    seedStoreProductManagement.controller('ModalProductController', ['$scope', '$uibModalInstance', 'product', 'categories', 'modalTitle', function ($scope, $uibModalInstance, product, categories, modalTitle) {
        $scope.modalTitle = modalTitle;
        $scope.product = product;
        $scope.categories = categories;
        $scope.ok = function () {
            $uibModalInstance.close($scope.product);
        };
        $scope.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };
    }]);

    seedStoreProductManagement.controller('ProductsManagementController', ['$scope', '$uibModal', 'ProductService', 'NotificationService', 'CategoryService', function ($scope, $uibModal, productService, notificationService, categoryService) {

        function modal(title, size, template, controller, product, callback) {
            var modalInstance = $uibModal.open({
                templateUrl: template,
                controller: controller,
                backdrop: true,
                size: size,
                resolve: {
                    product: function () {
                        return product;
                    },
                    modalTitle: function () {
                        return title;
                    },
                    categories: function () {
                        return $scope.categories;
                    }
                }
            });
            modalInstance.result.then(function (data) {
                callback(data);
            });
        }

        function getProductsSuccess(data) {
            $scope.paginatedProducts = data;
            $scope.pagination.totalServerItems = $scope.paginatedProducts.$viewInfo.totalSize;
            if ($scope.paginatedProducts.length) {
                $scope.activeProduct = $scope.paginatedProducts[0];
            }
        }

        function getProductsError(err) {
            throw new Error('could not get products list ' + err);
        }

        function getCategories() {
            categoryService.allCategories(function (categories) {
                $scope.categories = categories;
            });
        }

        function searchProducts(searchString) {
            if (searchString) {
                productService.searchPaginatedProducts(null, searchString, $scope.pagination.currentPage, $scope.pagination.pageSize, getProductsSuccess, getProductsError);
            } else {
                getProducts();
            }
        }

        function getProducts() {
            productService.allPaginatedProducts($scope.pagination.currentPage, $scope.pagination.pageSize, getProductsSuccess, getProductsError);
        }

        $scope.pageChanged = function () {
            getProducts();
        };

        $scope.setActiveProduct = function (product) {
            $scope.activeProduct = product;
        };

        $scope.pagination = {
            currentPage: 1,
            pageSize: 6,
            totalServerItems: 0
        };

        $scope.createNewProduct = function () {
            modal('Add a new product', 'lg', 'modalProduct.html', 'ModalProductController', {}, function (newProduct) {
                productService.addProduct(newProduct,
                    function () {
                        getProducts();
                    },
                    function (err) {
                        throw new Error('Could not add new product ' + err);
                    });
            });
        };

        $scope.editProduct = function (product) {
            modal('Edit product', 'lg', 'modalProduct.html', 'ModalProductController', product, function (product) {
                productService.updateProduct(product,
                    function () {
                        getProducts();
                    },
                    function (err) {
                        throw new Error('Could not update product ' + err);
                    });
            });
        };

        $scope.deleteProduct = function (product) {
            modal('Delete a product', 'sm', 'modalConfirmProduct.html', 'ModalProductController', product, function (product) {
                productService.deleteProduct(product,
                    function () {
                        getProducts();
                    },
                    function (err) {
                        throw new Error('Could not delete product ' + err);
                    });
            });
        };

        $scope.$watch('searchedProduct', function (newSearch, oldSearch) {
            if (newSearch !== oldSearch) {
                searchProducts(newSearch);
            }
        });

        getProducts();
        getCategories();
    }]);


    return {
        angularModules: ['ProductManagement']
    };
});
