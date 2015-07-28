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
    ],
    function () {
      "use strict";

      dataSourceService.$inject = [ '$resource' ];
      function dataSourceService( $resource ) {
        // TODO: resolve base url ?
        //var baseUrl = '/cxf/DataExplorerTool/preview';
        //var baseUrl = (CONTEXT_PATH === '/' ? '/pentaho/osgi/' : CONTEXT_PATH) + 'cxf/DataExplorerTool/det';

        // baseURL BA server
        var baseUrl = CONTEXT_PATH + 'osgi/cxf/DataExplorerTool/det';

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