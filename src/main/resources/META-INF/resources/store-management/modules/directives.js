/*
 * Copyright (c) 2013-2016, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
define([
    'require',
    '{lodash}/lodash',
    '{angular}/angular',
    '[text]!{store-management}/templates/navbar.html'
], function (require, _, angular, navbarTemplate) {
    'use strict';

    var seedStoreDirectives = angular.module('storeManagementDirectives', []);

    seedStoreDirectives.directive('seedStoreNavbar', [function () {
        return {
            restrict: 'A',
            template: navbarTemplate,
            replace: true
        };
    }]);

    seedStoreDirectives.config(['$httpProvider', function ($httpProvider) {
        $httpProvider.defaults.transformResponse.push(function (data) {
          if (typeof data.totalSize !== 'undefined' && typeof data.items !== 'undefined' && data.items instanceof Array) {
              var prototype = Object.getPrototypeOf(data.items);

              prototype.$viewInfo = _.extend({}, data);
              delete prototype.$viewInfo.items;

              return data.items;
          }
          return data;
        });
    }]);

    return {
        angularModules: ['storeManagementDirectives']
    };
});
