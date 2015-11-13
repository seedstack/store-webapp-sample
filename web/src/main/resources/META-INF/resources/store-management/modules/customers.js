/*
 * Copyright (c) 2013-2015 by The SeedStack authors. All rights reserved.
 *
 * This file is part of SeedStack, An enterprise-oriented full development stack.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
define([
    'require',
    'module',
    '{angular}/angular'
], function (require, module, angular) {
    'use strict';

    var seedStoreCustomerManagement = angular.module('CustomerManagement', []),
        config = module && module.config() || {};

    seedStoreCustomerManagement.factory('CustomerService', ['$resource', function ($resource) {
        var Customer = $resource(require.toUrl(config.apiUrl + 'customers/:customerId'), {}, {
            'update': {method: 'PUT'}
        });

        return {
            allPaginatedCustomers: function (pageIndex, pageSize, success, error) {
                return Customer.query({pageIndex: pageIndex - 1, pageSize: pageSize}, success, error);
            },
            searchPaginatedCustomers: function (searchString, pageIndex, pageSize, success, error) {
                return Customer.query({
                    searchString: searchString,
                    pageIndex: pageIndex - 1,
                    pageSize: pageSize
                }, success, error);
            },
            customer: function (id, success, error) {
                return Customer.get({customerId: id}, success, error);
            },
            deleteCustomer: function (customer, success, error) {
                customer.$delete({customerId: customer.id}, success, error);
            },
            addCustomer: function (customer, success, error) {
                var newCustomer = new Customer();

                newCustomer.id = customer.id;
                newCustomer.firstName = customer.firstName;
                newCustomer.lastName = customer.lastName;
                newCustomer.password = customer.password;
                newCustomer.address = customer.address;
                newCustomer.deliveryAddress = customer.deliveryAddress;

                newCustomer.$save(success, error);
            },
            updateCustomer: function (customer, success, error) {
                customer.$update({customerId: customer.id}, success, error);
            }
        };
    }]);

    seedStoreCustomerManagement.controller('ModalCustomerController', ['$scope', '$modalInstance', 'customer', 'modalTitle', function ($scope, $modalInstance, customer, modalTitle) {
        $scope.modalTitle = modalTitle;
        $scope.customer = customer;
        $scope.ok = function () {
            $modalInstance.close($scope.customer);
        };
        $scope.cancel = function () {
            $modalInstance.dismiss('cancel');
        };
    }]);

    seedStoreCustomerManagement.controller('CustomersManagementController', ['$scope', '$modal', 'CustomerService', function ($scope, $modal, customerService) {

        function modal(title, template, controller, customer, callback) {
            var modalInstance = $modal.open({
                templateUrl: template,
                controller: controller,
                resolve: {
                    customer: function () { return customer; },
                    modalTitle: function() { return title; }
                }
            });
            modalInstance.result.then(function(data) { callback(data); });
        }

        function getCustomersSuccess(data) {
            $scope.paginatedCustomers = data;
            $scope.pagination.totalServerItems = $scope.paginatedCustomers.$viewInfo.resultSize;
        }

        function getCustomersError(err) {
            throw new Error('could not get customers list ' + err.message);
        }

        function getCustomers() {
            customerService.allPaginatedCustomers($scope.pagination.currentPage, $scope.pagination.pageSize, getCustomersSuccess, getCustomersError);
        }

        function searchCustomers(searchString) {
            if (searchString) {
                customerService.searchPaginatedCustomers(searchString, $scope.pagination.currentPage, $scope.pagination.pageSize, getCustomersSuccess, getCustomersError);
            } else {
                getCustomers();
            }
        }

        $scope.pagination = {
            pageSize: 10,
            currentPage: 1,
            totalServerItems: 0
        };

        $scope.pageChanged = function() {
            getCustomers();
        };

        $scope.createNewCustomer = function() {
            modal('Add a customer', 'modalCustomer.html', 'ModalCustomerController', {}, function(newCustomer) {
                customerService.addCustomer(newCustomer,
                    function() {
                        getCustomers();
                    },
                    function(err) {
                        throw new Error('Could not add new customer ' + err.message);
                    });
            });
        };

        $scope.editCustomer = function(customer) {
            modal('Edit customer', 'modalCustomer.html', 'ModalCustomerController', customer, function (customer) {
                customerService.updateCustomer(customer,
                    function () {
                        getCustomers();
                    },
                    function (err) {
                        throw new Error('Could not update customer ' + err.message);
                    });
            });
        };

        $scope.deleteCustomer = function(customer) {
            modal('Delete a customer', 'modalConfirmCustomer.html', 'ModalCustomerController', customer, function (customer) {
                customerService.deleteCustomer(customer,
                    function() {
                        getCustomers();
                    },
                    function(err) {
                        throw new Error('Could not delete customer ' + err.message);
                    });
            });
        };

        $scope.$watch('searchedCustomer', function(newSearch, oldSearch) {
            if (newSearch !== oldSearch) {
                searchCustomers(newSearch);
            }
        });

        getCustomers();
    }]);

    return {
        angularModules: ['CustomerManagement']
    };
});
