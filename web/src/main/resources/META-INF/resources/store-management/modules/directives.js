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
    '{angular}/angular',
    '[text]!{store-management}/templates/navbar.html'
], function (require, angular, navbarTemplate) {
    'use strict';

    var seedStoreDirectives = angular.module('storeManagementDirectives', []);

    seedStoreDirectives.directive('seedStoreNavbar', [function() {
      return {
          restrict: 'A',
          template: navbarTemplate,
          replace: true
      };
    }]);

    return {
        angularModules: ['storeManagementDirectives']
    };
});
