require.config({
    "paths": {
        "angular"           : "/webjars/angular",
        "angular-ui-router" : "/webjars/angular-route",
        "angular-resource"  : "/webjars/angular-resource",
        "angular-mocks"     : "/webjars/angular-mocks",

        "underscorejs"      : "/webjars/underscore",
        "text"              : "/webjars/text",

        "ui-router-state-helper" : "/ui-router-state-helper/stateHelper",
        "service"                : "/lib/service/service"
    },

    "shim" : {
        "angular"           : { "exports" :  "angular"  },
        "angular-ui-router" : { "deps"    : ["angular"] },
        "angular-resource"  : { "deps"    : ["angular"] },
        "angular-mocks"     : { "deps"    : ["angular"] },

        "ui-router-state-helper" : { "deps"    : ["angular"] }
    }
});