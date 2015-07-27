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