'use strict';

describe('Controller Tests', function() {

    describe('Defect Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockDefect, MockPlugin;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockDefect = jasmine.createSpy('MockDefect');
            MockPlugin = jasmine.createSpy('MockPlugin');


            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Defect': MockDefect,
                'Plugin': MockPlugin
            };
            createController = function() {
                $injector.get('$controller')("DefectDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'lighthouseApp:defectUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
