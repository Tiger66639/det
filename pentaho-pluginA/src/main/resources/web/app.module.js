/*
 * This program is free software; you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License, version 2.1 as published by the Free Software
 * Foundation.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this
 * program; if not, you can obtain a copy at http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 * or from the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * Copyright (c) 2015 Pentaho Corporation. All rights reserved.
 */


define(
    [
      'common-ui/angular',
      'dataTable/dataTable.controller'
    ],
    function ( angular, DataTableController

    ) {
      "use strict";

      var dataModule = angular.module( 'com.pentaho.det.data', [ 'ui.router', 'smart-table' ]);

      /*
      plugin.config( [ '$stateProvider', '$urlRouterProvider',
        function( $stateProvider ) {

        $stateProvider
            .state( 'Data', {
              url: "/pluginA",
              templateUrl: "/DataExplorerToolPluginA/web/partials/plugin.html"
            });
      }]);
      */

      var moduleStates = [
        {
          name: 'Data',
          templateUrl: '/DataExplorerToolPluginA/web/dataTable/dataTable.html',
          controller: 'com.pentaho.det.data.DataTableController as viewModel'
        }
      ];

      return {
        name: dataModule.name,
        states: moduleStates,
        module: dataModule
      };
    }
);