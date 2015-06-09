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

define(
    [
      'common-ui/angular',
      'underscorejs',

      './app.controller',
      //'text!./app.html',

      'service!IDetPlugin',
      'angular-ui-router',
      'ui-router-state-helper',
      'common-ui/angular-resource',
    ],
    function ( angular,
               _,
               applicationController,
               //appHtml,
               detPlugins ) {
      "use strict";

      var moduleName = 'detApp';
      var moduleDependencies = [ 'ui.router', 'ui.router.stateHelper', 'ngResource' ];
      var detPluginModuleNames = _.pluck( detPlugins, 'name' );

      moduleDependencies = _.union( moduleDependencies, detPluginModuleNames );

      var moduleRootStates = _.pluck( detPlugins, 'states' );

      var detApp = angular
          .module( moduleName, moduleDependencies )
          //.controller( moduleName + 'ApplicationController', applicationController )
          .config( config )
          .run( function ( $rootScope ) {
            //var $rootScope = angular.element(document.querySelectorAll("[ui-view]")[0]).injector().get('$rootScope');

            $rootScope.$on('$stateChangeStart',function(event, toState, toParams, fromState, fromParams){
              console.log('$stateChangeStart to '+toState.to+'- fired when the transition begins. toState,toParams : \n',toState, toParams);
            });

            $rootScope.$on('$stateChangeError',function(event, toState, toParams, fromState, fromParams){
              console.log('$stateChangeError - fired when an error occurs during transition.');
              console.log(arguments);
            });

            $rootScope.$on('$stateChangeSuccess',function(event, toState, toParams, fromState, fromParams){
              console.log('$stateChangeSuccess to '+toState.name+'- fired once the state transition is complete.');
            });

            $rootScope.$on('$viewContentLoaded',function(event){
              console.log('$viewContentLoaded - fired after dom rendered',event);
            });

            $rootScope.$on('$stateNotFound',function(event, unfoundState, fromState, fromParams){
              console.log('$stateNotFound '+unfoundState.to+'  - fired when a state cannot be found by its name.');
              console.log(unfoundState, fromState, fromParams);
            });
          });





      config.$inject = [ 'stateHelperProvider', '$urlRouterProvider' , '$stateProvider' ];
      function config( stateHelperProvider, $urlRouterProvider, $stateProvider ) {
        var rootURL = '/det';
        // For any unmatched url, send to root url
        $urlRouterProvider.otherwise( rootURL );

        /*
        $stateProvider
            .state( 'mainView', {
              url: "/mainView",
              templateUrl: "partials/pluginView.html",
              controller: function( $scope, previewDataProvider ) {
                $scope.previewData = previewDataProvider.getDataFromDataSource( "stepA" );
                $scope.getRowValue = function ( columnIdx ) {
                  return function ( row ) {
                    if ( row.c && row.c[columnIdx] ) {
                      return row.c[columnIdx].v;
                    }

                    return undefined;
                  }
                };

                $scope.displayRowCollection = [];
              }
            });
        */

        stateHelperProvider
        //$stateProvider
            .state( {
              //abstract: true,
              name: 'detApp',
              url: rootURL,
              templateUrl: 'js/app.html',
              //template: appHtml,
              controller: applicationController,
              controllerAs: 'appViewModel',
              children: moduleRootStates
            });
      }

      return {
        module: detApp,

        init: function () {
          angular.element( document ).ready( function () {
            angular.bootstrap( document, [ moduleName ]);


          });
        }
      };

    }
);