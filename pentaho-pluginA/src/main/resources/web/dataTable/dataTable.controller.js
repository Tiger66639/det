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
    ],
    function( ) {
      "use strict";

      DataTableController.$inject = [ 'moduleChildren', 'com.pentaho.det.dataA.dataSourceService' ];
      function DataTableController( moduleChildren, dataSourceService ) {
        var viewModel = this;
        viewModel.title = 'DataTableController';
        viewModel.activate = activate;

        viewModel.getRowValue = getRowValue;
        viewModel.previewData = {};
        viewModel.displayRowCollection = [];

        activate();

        ////////////////

        function activate() {
          viewModel.previewData = dataSourceService.getDataFromDataSource( "stepA" );
          viewModel.moduleChildren = moduleChildren;
        }

        function getRowValue ( columnIdx ) {
          return function ( row ) {
            if ( row.c && row.c[columnIdx] ) {
              return row.c[columnIdx].v;
            }

            return undefined;
          }
        }

      }

      // TODO: return DataTableController or angular controller? are they the same?
      return DataTableController;
    }

);