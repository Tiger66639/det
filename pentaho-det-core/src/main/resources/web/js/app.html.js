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

define([], function() {
    return '' +
        '<div class="pentaho.det.app">\n' +
        '  <!-- div data-ng-controller="com.pentaho.det.ApplicationController as appViewModel" -->\n\n' +
        '    <div class="navbar">\n' +
        '      <div class="navbar-inner">\n' +
        '        <a class="brand" href="#">Home</a>\n' +
        '        <ul class="nav">\n' +
        '          <li data-ng-repeat="state in appViewModel.pluginStates">\n' +
        '            <a data-ui-sref={{state.name}}>{{state.name}}</a>\n' +
        '          </li>\n' +
        '        </ul>\n' +
        '      </div>\n' +
        '    </div>\n\n' +
        '    <div class="row">\n' +
        '      <div class="span12">\n' +
        '        <div class="well" data-ui-view></div>\n' +
        '      </div>\n' +
        '    </div>\n\n' +
        '  <!--/div-->\n\n' +
        '</div>';
});