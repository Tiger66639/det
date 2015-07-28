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
        './visual.controller',
        'text!./visual.html',
        'pentaho/visual/Wrapper',
        'pentaho/visual/type/registry',

        'underscorejs'
    ],
    function( controller, template, VisualWrapper, typeRegistry, _ ) {
      "use strict";

      VisualDirective.$inject = [];
      function VisualDirective () {

        var directive = {
          restrict: 'E',
          replace: true, //replaces the custom directive element with the corresponding expanded HTML, to be HTML-compliant.
          template: template,
          controller: controller,
          controllerAs: 'viewModel',
          link: link,

          scope: {
            datatable: '=',
            visualType: '@'
          }
        };

        return directive;

        //////////////////////////////////////

        function link( scope, element, attributes ) {
          var REQUIREMENT_ATTRIBUTE_PREFIX = 'requirement';
          var visualWrapper = new VisualWrapper( element[0] );
          visualWrapper.visualSpec = {
            type: scope.visualType
          };

          var requirementAttributes = getRequirementAttributes( attributes );
          // Update the new requirement value on the scope viewModel and render the visual every time a requirement attribute changes its value
          _.each( requirementAttributes,
              function ( requirementAttributeValue, requirementAttributeKey ) {
                var requirementId = getRequirementId( requirementAttributeKey );
                if ( requirementId !== undefined ) {
                  scope.$watch(
                      function() { return scope.$parent.$eval( requirementAttributeValue ); },
                      function( requirementNewValue ) {
                        scope.viewModel.requirements[ requirementId ] = requirementNewValue;
                        render();
                      }
                  );
                }
              }
          );

          scope.$on( '$destroy', dispose );
          scope.datatable.$promise.then( render );

          ////////////////

          function dispose() {
            visualWrapper.dispose();
          }

          function render() {
            if ( scope.datatable.$resolved ) {
              visualWrapper.data = scope.datatable;
              visualWrapper.visualSpec = {
                type: scope.visualType
              };
              _.extend( visualWrapper.visualSpec, scope.viewModel.requirements );

              return visualWrapper.update(); // returns promise (ES6) resolves when rendered with no value (undefined)
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
            var candidateRequirementName = requirementAttributeName.replace( regExp ,"");

            var requirementIds = _.chain ( visualWrapper.visualType.dataReqs )
                .pluck( 'reqs' )  // select requirements from requirement set
                .flatten()        // merge all requirements from different requirement sets
                .pluck( 'id' )    // get requirement ids
                .unique()         // eliminate duplicates
                .value();

            // find the matching Id in all the requirements defined for the visual type
            // Note: this method does not differentiate between distinct requirements which name only differs in casing. E.g. "amazingRequirement" and "AmaZingReQuiRement"
            return _.find( requirementIds, _.partial( areCaseInsensitiveEqual, candidateRequirementName ) );
          }

          function areCaseInsensitiveEqual( stringA, stringB ) {
            return stringA.toUpperCase() === stringB.toUpperCase();
          }

        }
      }

      return VisualDirective;
    }

);