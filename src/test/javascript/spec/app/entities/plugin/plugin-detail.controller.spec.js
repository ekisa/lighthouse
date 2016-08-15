'use strict';

describe('Controller Tests', function() {

    describe('Plugin Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPlugin, MockDefect, Mockplugin;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPlugin = jasmine.createSpy('MockPlugin');
            Mockplugin = jasmine.createSpy('Mockplugin');
            MockDefect = jasmine.createSpy('MockDefect');


            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Plugin': MockPlugin,
                'plugin': Mockplugin,
                'Defect': MockDefect
            };
            createController = function() {
                $injector.get('$controller')("PluginDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'lighthouseApp:pluginUpdate';

                createController();
                //expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
