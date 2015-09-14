define(['common-ui/angular', 'app.controller'], function(angular, ctrlClass) {

    describe('Require Hello World Resource Folder', function() {
        it('Hello is not World', function() {
            expect('Hello' != 'World').toBe(true);
        });

        it('angular.js is loaded and is 1.2 branch', function() {
            expect(angular.version.major).toBe(1);
            expect(angular.version.minor).toBe(2);
        });

        it('controller is ApplicationController', function() {
            var ctrl = new ctrlClass();
            expect(ctrl.title).toBe('ApplicationController');
        });
    });

});
