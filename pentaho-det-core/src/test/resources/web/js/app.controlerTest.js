define(['js/app.controller'], function(ctrlClass) {

    describe('App Controller Test', function() {
        it('controller is ApplicationController', function() {
            var ctrl = new ctrlClass();
            expect(ctrl.title).toBe('ApplicationController');
        });
    });
});
