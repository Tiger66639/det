/*
 * Copyright 2002 - 2015 Webdetails, a Pentaho company. All rights reserved.
 *
 * This software was developed by Webdetails and is provided under the terms
 * of the Mozilla Public License, Version 2.0, or any later version. You may not use
 * this file except in compliance with the license. If you need a copy of the license,
 * please go to http://mozilla.org/MPL/2.0/. The Initial Developer is Webdetails.
 *
 * Software distributed under the Mozilla Public License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. Please refer to
 * the license for the specific language governing your rights and limitations.
 */


define(
    [
      'angular',

      // TODO: add resource
      'text!./dataTable/dataTable.html',
      './dataTable/dataTable.controller',
      './services/dataSourceService',

      'angular-ui-router',
      'angular-resource',
      'smart-table'

    ],
    function ( angular, appModuleHtml, DataTableController, dataSourceService

    ) {
      "use strict";

      var ngModuleName = 'com.pentaho.det.dataB';

      var dataModule = angular
          .module( ngModuleName, [ 'ui.router', 'smart-table', 'ngResource' ] )
          .factory( ngModuleName + '.dataSourceService', dataSourceService );


      var moduleStateChildren = [{
        name: "DataB_Child",
        template: '<div class="well"><h1>I\'m a child state from DataB</h1><a data-ui-sref=".DataB_Child_N2">DataB_Child_N2</a><br><div data-ui-view></div></div>',
        children: [{
          name: 'DataB_Child_N2',
          template: '<div class="well"><h1>I\'m a child state from DataB_Child</h1></div>'
        }]
      }];

      var moduleStates = [
        {
          name: 'DataB',
          template: appModuleHtml,
          controller: DataTableController,
          controllerAs: 'viewModel',
          resolve: {
              moduleChildren: function() { return moduleStateChildren; }
          },
          children: moduleStateChildren
        }
      ];

      return {
        name: dataModule.name,
        states: moduleStates,
        module: dataModule
      };
    }
);