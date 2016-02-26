'use strict';

describe('Controller Tests', function() {

    describe('Project Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockProject, MockDefect;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockProject = jasmine.createSpy('MockProject');
            MockDefect = jasmine.createSpy('MockDefect');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Project': MockProject,
                'Defect': MockDefect
            };
            createController = function() {
                $injector.get('$controller')("ProjectDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'lighthouseApp:projectUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
