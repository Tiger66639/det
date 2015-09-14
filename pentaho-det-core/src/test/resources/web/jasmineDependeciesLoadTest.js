define(['angular'], function(angular) {
    describe('Checking test dependencies loaded by jasmine maven plugin', function() {
        it('angular.js is loaded and is 1.2 branch', function() {
            expect(angular.version.major).toBe(1);
            expect(angular.version.minor).toBe(2);
        });
    });
});