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
        './visualization.controller',
        'text!./visualization.html',
        'pentaho/visual/Wrapper'
    ],
    function( controller, template, VisualWrapper ) {
      "use strict";

      VisualizationDirective.$inject = [];
      function VisualizationDirective () {

        var directive = {
          restrict: 'E',
          replace: true, //replaces the custom directive element with the corresponding expanded HTML, to be HTML-compliant.
          //templateUrl: 'directives/pluginList/pluginListTemplate.html',
          template: template,
          controller: controller,
          controllerAs: 'viewModel',
          link: link,

          //isolate scope
          scope: {
            //we could use a different name for the html attribute, using "=html-property" instead of "="
            //'&' evaluates in the parent scope
            //'@' evaluates as a string
            //'=' evaluates in the isolate scope
            datatable: '=',
            visualizationType: '@',
            visualizationScopeRoleCalc: '='
          }
        };

        return directive;

        //////////////////////////////////////

        function link( scope, element, attributes ) {

          var domElement = element[0];
          var visualizationWrapper = new VisualWrapper( domElement );

          // TODO check whether to use scope or attributes
          //scope.$watch( 'datatable' , render );
          //scope.$watch( 'visualizationType', render );

          //scope.$watch( attributes.visualizationRoleCalc, render );

          scope.$watch( function() {
            var vizCalc = scope.$parent.$eval( attributes.visualizationRoleCalc );
            return vizCalc;
          }, render );

          function onPropertyChange ( propertyName, newValue ) {
            scope[ propertyName]  = asd;
          }

          //attributes.$observe( 'visualizationRoleCalc', render );

          //scope.$watch( 'visualizationScopeRoleCalc', render );


          //render();

          //scope.datatable.$promise.then( render );

          ////////////////

          function render() {
            if ( scope.datatable.$resolved ) {
              // TODO check whether to use scope or attributes
              visualizationWrapper.data = scope.datatable;

              visualizationWrapper.visualSpec = {
                type: scope.visualizationType,
                calc: scope.$parent.$eval( attributes.visualizationRoleCalc )
                //calc: scope.visualizationScopeRoleCalc
              };
              return visualizationWrapper.update(); // returns promise (ES6) resolves when rendered with no value (undefined)
            }
          }

          ctrl.render = render;

          function getValue( attrName ) {
            return scope.$parent.$eval( attributes[PREFIX+ attrName] );

          }

        }
      }

      return VisualizationDirective;
    }

);