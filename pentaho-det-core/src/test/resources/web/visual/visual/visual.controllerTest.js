define(['visual/visual/visual.controller'], function(ctrlClass) {

    describe('Visual Controller Test', function() {
        it('controller is VisualController', function() {
            var ctrl = new ctrlClass();
            expect(ctrl.title).toBe('VisualController');
        });

        it('controller requirements should be empty', function() {
            var ctrl = new ctrlClass();
            expect(ctrl.requirements).toEqual({});
        });
    });

});
