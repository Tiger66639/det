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
      'underscorejs',

      './app.controller',
      'text!./app.html',
      '../visual/visual/visual.directive',

      'service!IDetPlugin',

      'angular-ui-router',
      'ui-router-state-helper',
      'angular-resource',
    ],
    function ( angular,
               _,
               applicationController,
               appHtml,
               visualDirective,
               detPlugins ) {
      "use strict";

      var moduleName = 'detApp';
      var moduleDependencies = [ 'ui.router', 'ui.router.stateHelper', 'ngResource' ];
      var detPluginModuleNames = _.pluck( detPlugins, 'name' );

      moduleDependencies = _.union( moduleDependencies, detPluginModuleNames );

      var moduleRootStates = _.chain( detPlugins )
          .pluck( 'states' )
          .flatten( true )
          .value();

      /*
      * Module creation and configuration
      */
      var detApp = angular
          .module( moduleName, moduleDependencies )
          //.controller( moduleName + 'ApplicationController', applicationController )
          .directive( 'pentahoVisual', visualDirective )
          .config( config )
          // TODO: remove debug logs
          .run( debugStates );

      debugStates.$inject = [ '$rootScope' ];
      function debugStates( $rootScope ) {
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
      }

      config.$inject = [ 'stateHelperProvider', '$urlRouterProvider' , '$stateProvider', '$httpProvider' ];
      function config( stateHelperProvider, $urlRouterProvider, $stateProvider, $httpProvider ) {
        var rootURL = '/det';
        // For any unmatched url, send to root url
        $urlRouterProvider.otherwise( rootURL );

        $httpProvider.defaults.withCredentials = true;

        var rootState = {
          name: moduleName,
          url: rootURL,
          template: appHtml,
          controller: applicationController,
          controllerAs: 'appViewModel',
          resolve: {
              moduleStates: function() { return moduleRootStates; }
          },
          children: moduleRootStates
        };

        stateHelperProvider.state( rootState );
      }

      return {
        module: detApp,

        init: detAppInit
      };

      ////////////////////

      function detAppInit( element ) {
        angular.element( element ).ready( function () {
          angular.bootstrap( element, [ moduleName ]);
        });
      }

    }
);