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
        '../app.module',
        'common-ui/angular-resource'
    ],
    function ( app,
               $resource
    ) {
      "use strict";

      var ngModuleName = app.name;
      app.module.factory( ngModuleName + 'dataSourceService', dataSourceService );

      dataSourceService.$inject = [ $resource.name ];

      function dataSourceService( $resource ) {
        // TODO: resolve base url ?
        var baseUrl = '/cxf/DataExplorerTool/preview';
        var DataSource = $resource( baseUrl + '/dataSources/:uuid',
            { uuid: '@uuid', name:'@name' },
            { getData: { method: 'GET', url: baseUrl + '/dataSources/:uuid/data' } }
        );

        return {
          getDataSources: getDataSources,

          getDataSource: getDataSource,

          getDataFromDataSource: getDataFromDataSource
        };

        ////////////////

        function getDataSources () {
          return DataSource.query();
        }

        function getDataSource ( uuid ) {
          return DataSource.get( { uuid: uuid } );
        }

        function getDataFromDataSource( dataSourceUuid ) {
          return DataSource.getData( {uuid: dataSourceUuid} );
        }

      }

      return dataSourceService;

    }
);