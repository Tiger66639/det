# Embedding Pentaho Data Exploration Tool (DET)
DET can be embedded into other web applications, but to do so you need to:
* Configure your BI Server to allow [Cross-Origin Resource Sharing](http://www.w3.org/TR/cors/) (CORS)
* Configure your web application to embed DET. 

## CORS Configuration

We need to do this configuration because the embedded DET will be requesting resources from your BI Server (domainA),
but those requests are coming from the server where you are embedding DET (domainB), and requests from different domains
are blocked by the browser for restricted resources, e.g. Javascript.
This is why we need CORS, that is a W3C spec to allow cross domain communication by adding a few special headers to both
requests and corresponding responses.

To configure your BI Server to enable CORS:

1. Stop the BI Server
2. Open the file `web.xml` inside the folder `biserver/tomcat/webapps/pentaho/WEB-INF/`
3. Search for `<!-- insert additional filters -->` and insert your CORS configuration here:
  ```
    <filter>
      <filter-name>CorsFilter</filter-name>
      <filter-class>org.apache.catalina.filters.CorsFilter</filter-class>
      <init-param>
        <param-name>cors.allowOrigin</param-name>
        <param-value>http://www.example.com</param-value> <!--This should be the server where you are embedding DET (domainB)-->
      </init-param>
      <init-param>
        <param-name>cors.support.credentials</param-name>
        <param-value>true</param-value>
      </init-param>
    </filter>
    <filter-mapping>
      <filter-name>CorsFilter</filter-name>
      <url-pattern>/*</url-pattern>
    </filter-mapping>
  ```
4. Save the file and start the BI Server.

This is the minimum configuration to have your pentaho server ready for CORS, for a more detailed configuration
check [Tomcat Documentation on CORS Filters](https://tomcat.apache.org/tomcat-7.0-doc/config/filter.html#CORS_Filter)

## Embedding Application Configuration

With the configuration done on your BI Server all your responses are ready for CORS. Now the configuration of your
embedding application will also involve making sure all its requests are ready for CORS but the major part will be
configuring how to get the resources needed to run DET and also how to bootstrap it.

Here we have two examples of different use case scenarios on how to embed DET:

* #### Bootstrap Full DET
  This is simplest case, where you only need to include a script to configure the location of your BI Server, in the
  example it is the variable `pentaho_host` and request `require-init.js` that as the name indicates will take care of
  initializing and configuring require that we will be later using to request all the resources needed. Finally we only
  need to make the `text` plugin ready for CORS and bootstrap our application as seen below. Full example [here]()

  ```
  <script type="text/javascript">
    //Replace @PENTAHO_SERVER_HOST@ with the location of your pentaho server
    var pentaho_host = "@PENTAHO_SERVER_HOST@";
    var CONTEXT_PATH = pentaho_host;

    document.write("<script language='javascript' type='text/javascript' src='" + CONTEXT_PATH + "osgi/requirejs-manager/js/require-init.js'></scr"+"ipt>");
    document.write("<script language='javascript' type='text/javascript' src='" + CONTEXT_PATH + "content/common-ui/resources/web/common-ui-require-js-cfg.js'></scr"+"ipt>");
  </script>

  <script type="text/javascript">
    requireCfg.baseUrl = CONTEXT_PATH = pentaho_host + "osgi/";
    requireCfg.config.text = {
      onXhr: function(xhr, url) {
        xhr.withCredentials = true;
      }
    };

    require.config( requireCfg );

    require([ "com.pentaho.det/js/app" ], function( app ) {
      app.init( document );
    });
  </script>
  ```

* #### Bootstrap Multiple DET Modules
  The only different from the last example is how to bootstrap DET, because now we are not requesting the full
  application but just independent modules, so we need to create a simple angular application with a root state so that
  the DET Modules can be configured to load inside that state. Complete example [here]().

  In this case we use some auxiliary functions that makes the configuration easier. In the complete example this
  functions are where the comment `Insert utility functions here` is in the snippet bellow. We will cover those
  in other section.

  ```
  require([ 
    "angular",

    "com.pentaho.det.pluginA/app.module",
    "com.pentaho.det.pluginB/app.module",

    'underscorejs',

    'angular-ui-router',
    'ct-ui-router-extras',
    'ui-router-state-helper'
    ], 

    function( angular, pluginA, pluginB, _ ) {
      var myModuleName = 'webApp';
      //Your angular app dependencies
      var moduleDeps = [ 'ui.router', 'ct.ui.router.extras', 'ui.router.stateHelper',
                          pluginA.name, pluginB.name ];
      angular
        .module(myModuleName, moduleDeps )
        .config(config)
        .run(run);

      //Get each state from the DET modules
      var stateA = getModuleState(pluginA);
      var stateB = getModuleState(pluginB);

      //Configure your angular application here
      config.$inject = [ 'stateHelperProvider', '$urlRouterProvider', '$stateProvider',
                         '$httpProvider' ];
      function config( stateHelperProvider, $urlRouterProvider, $stateProvider, $httpProvider ) {
        $urlRouterProvider.otherwise( '/' );

        //$http request default configuration for CORS
        $httpProvider.defaults.withCredentials = true;

        //Create a named view for each state, so that they can be loaded in parallel
        //in the same parent state
        var viewsMapping = {};
        viewsMapping[stateA.name] = "pluginA";
        viewsMapping[stateB.name] = "pluginB";

        //This will create/rename the necessary views on all states of your application
        var rootChildren = configStates([stateA, stateB], viewsMapping);

        //The template must have a view for each module, with the currect view name
        var rootTemplate = '' + 
          '<div data-ui-view="' + viewsMapping[stateA.name] + '" class="well"></div>' +
          '<br>' +
          '<div data-ui-view="' + viewsMapping[stateB.name] + '" class="well"></div>';

        //Basic root state for embending angular app
        var rootState = {
          name: myModuleName,
          template: rootTemplate,
          children: rootChildren
        };

        //This will create your navigation tree for your application
        stateHelperProvider.state( rootState );
      };

      //Tell your application what to do when it starts running
      run.$inject = ['$state'];
      function run($state) {
        //load both modules into the web page
        initStates($state, [stateA, stateB]);
      }

      //Bootstrap your application
      angular.element( document ).ready( function () {
        angular.bootstrap( document, [ myModuleName ]);
      });

      /**
       * Insert utility functions here
       */
    });
  ```

## Using your own angular with DET

If you are already using angular, it is possible to configure DET to use your version instead of its own. And it
is really easy to do so, because you only need to add this lines after configuring the `text` plugin, replace
`@ANGULAR_SCRIPTS_LOCATION@` with the path where your angular scripts are located and the previous examples would
be using your own angular version:

```
//Replace @ANGULAR_SCRIPTS_LOCATION@ with the location of your angular scripts
requireCfg['paths']['app/angular'] = "@ANGULAR_SCRIPTS_LOCATION@/angular";
requireCfg['paths']['app/angular-resource'] = "@ANGULAR_SCRIPTS_LOCATION@/angular-resource";
requireCfg['shim']['app/angular'] = {
  deps: ['common-ui/jquery'],
  exports: 'angular'
};
requireCfg['shim']['app/angular-resource'] = ['app/angular'];

requireCfg.map = requireCfg.map || {};
requireCfg.map["*"] = requireCfg.map["*"] || {};

requireCfg.map["*"]['angular'] = "app/angular";
requireCfg.map["*"]['angular-resource'] = "app/angular-resource";
```

Here you can see the complete examples of how to bootstrap DET using your angular version:
* [Bootstrap Full DET]()
* [Bootstrap Multiple DET Modules]()


## Utility Functions

This are functions that will help making the configuration of the `Multiple DET Modules` use case easier.

```
/**
 * Function that will return the first state of the given DET module
 *
 * @param module DET module
 * @return {Object} First state of DET module
 */
function getModuleFirstState(module) {
  return module.states[0];
}

/**
 * Function that will load all the states passed as argument, in your web application page
 *
 * @param $state angular service
 * @param states array of states
 */
function initStates($state, states) {

  if(_.size(states)) {
    var firstState = _.first(states);
    var stateName = firstState.name;

    $state.go(stateName).then( function() {
      initStates($state, states.slice(1)) 
    });
  }

}

/**
 * Function that will configure all states and their children states,
 * using the viewsMapping object to create/rename views so that
 * the DET modules load to the desired places of the application
 *
 * @param states array of states
 * @param viewsMapping Object that contains the mapping between the original view name and the new name
 *                     you want to give it
 * @return {Array} Initial array of states correctly configured
 */
function configStates(states, viewsMapping ) {
  _.map(states, function(state, index) {
    var nViews = _.size(state.views);

    if(nViews == 0) {
      state = _mapInitialStateViews(state, viewsMapping);
    } else {
      state = _mapOtherStateViews(state, viewsMapping);
    }

    _.map(state.children, function(childState, index) {
      _mapOtherStateViews(childState, viewsMapping);
    });
  });
  
  return states;
}

/**
 * Auxiliary Function used by "configStates" to configure the states
 * that are direct children of the root state.
 *
 * @param state current state being configured
 * @param viewsMapping Object that contains the mapping between the original view name and the new name
 *                     you want to give it
 */
function _mapInitialStateViews(state, viewsMapping) {
  state.sticky = state.sdr = true;
  state.views = {};
  var viewName = viewsMapping[state.name];
  state.views[viewName] = {
    template: state.template,
    controller: state.controller,
    controllerAs: state.controllerAs
  }

  return state;
}

/**
 * Auxiliary Function used by "configStates" to configure 
 * all the states of the application
 *
 * @param state current state being configured
 * @param viewsMapping Object that contains the mapping between the original view name and the new name
 *                     you want to give it
 */
function _mapOtherStateViews(state, viewsMapping) {
  if(state.views === undefined) {
    return;
  }

  var stateViews = state.views;
  _.map(viewsMapping, function(originalViewName, newViewName) {
    var stateOriginalView = stateViews[originalViewName];
    
    stateViews[newViewName] = stateOriginalView;
    delete stateOriginalView; //deleting original view, because all of its information as been copied to the new one
  });
}
```