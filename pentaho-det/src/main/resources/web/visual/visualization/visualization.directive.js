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
        'pentaho/visual/Wrapper',

        'underscorejs'
    ],
    function( controller, template, VisualWrapper, _ ) {
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
            visualizationType: '@'
          }
        };

        return directive;

        //////////////////////////////////////

        function link( scope, element, attributes ) {
          var REQUIREMENT_ATTRIBUTE_PREFIX = 'requirement';
          var visualizationWrapper = new VisualWrapper( element[0] );

          var requirementAttributes = getRequirementAttributes( attributes );
          // Update the new requirement value on the scope viewModel and render the visualization every time a requirement attribute changes its value
          _.each( requirementAttributes,
              function ( requirementAttributeValue, requirementAttributeKey ) {
                var requirementId = getRequirementId( requirementAttributeKey );
                scope.$watch(
                    function() { return scope.$parent.$eval( requirementAttributeValue ); },
                    function( requirementNewValue ) {
                      scope.viewModel.requirements[ requirementId ] = requirementNewValue;
                      render();
                    }
                );
              }
          );

          scope.$on( '$destroy', dispose );
          scope.datatable.$promise.then( render );

          ////////////////

          function dispose() {
            visualizationWrapper.dispose();
          }

          function render() {
            if ( scope.datatable.$resolved ) {
              visualizationWrapper.data = scope.datatable;
              visualizationWrapper.visualSpec = {
                type: scope.visualizationType
              };
              _.extend( visualizationWrapper.visualSpec, scope.viewModel.requirements );

              return visualizationWrapper.update(); // returns promise (ES6) resolves when rendered with no value (undefined)
            }
          }

          // returns associative map of requirement attributes
          function getRequirementAttributes( attributes ) {
            var requirementAttributes = _.pick( attributes, function( value, key ) {
              return isRequirementAttribute( key );
            } );
            return requirementAttributes;
          }

          function isRequirementAttribute( attributeName ) {
            return attributeName.indexOf( REQUIREMENT_ATTRIBUTE_PREFIX ) == 0;
          }

          function getRequirementId( requirementAttributeName ) {
            var regExp = new RegExp( '^(' + REQUIREMENT_ATTRIBUTE_PREFIX + ')' );
            return requirementAttributeName
                .replace( regExp ,"")
                // TODO: match requirement Id with info from visual type instead of lowercasing
                .toLowerCase();
          }
        }
      }

      return VisualizationDirective;
    }

);