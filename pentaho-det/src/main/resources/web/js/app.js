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

/*!
 * PENTAHO CORPORATION PROPRIETARY AND CONFIDENTIAL
 *
 * Copyright 2002 - 2015 Pentaho Corporation (Pentaho). All rights reserved.
 *
 * NOTICE: All information including source code contained herein is, and
 * remains the sole property of Pentaho and its licensors. The intellectual
 * and technical concepts contained herein are proprietary and confidential
 * to, and are trade secrets of Pentaho and may be covered by U.S. and foreign
 * patents, or patents in process, and are protected by trade secret and
 * copyright laws. The receipt or possession of this source code and/or related
 * information does not convey or imply any rights to reproduce, disclose or
 * distribute its contents, or to manufacture, use, or sell anything that it
 * may describe, in whole or in part. Any reproduction, modification, distribution,
 * or public display of this information without the express written authorization
 * from Pentaho is strictly prohibited and in violation of applicable laws and
 * international treaties. Access to the source code contained herein is strictly
 * prohibited to anyone except those individuals and entities who have executed
 * confidentiality and non-disclosure agreements or other agreements with Pentaho,
 * explicitly covering such access.
 */

'use-strict';

define(
    [
      'common-ui/angular',
      'common-ui/angular-route',
      'common-ui/angular-resource',

      './services/previewDataProvider',
      './applicationController'
    ],
    function ( angular, angularRoute, angularResource,
               previewDataProvider, applicationController ) {

      var detApp = angular.module( 'detApp', [ 'ngRoute', 'ngResource' ] );

      detApp.config( [ '$routeProvider', function( $routeProvider ) {

        $routeProvider.when( '/',
            {
              templateUrl: 'partials/applicationView.html',
              controller: 'applicationController'
            });

        $routeProvider.otherwise(
            {
              redirectTo: '/'
            });
      }]);

      // dependency is injected by argument name convention
      detApp.factory( 'previewDataProvider', previewDataProvider );

      detApp.controller( 'applicationController',  applicationController );


      return {
        module: detApp,

        init: function () {
          angular.element( document ).ready( function () {
            angular.bootstrap( document, [ 'detApp' ]);
          });
        }
      };
    }
);