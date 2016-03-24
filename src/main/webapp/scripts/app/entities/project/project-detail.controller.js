'use strict';

angular.module('lighthouseApp')
    .controller('ProjectDetailController',
            function ($scope, $rootScope, $stateParams, project, Project, ParseLinks) {
                $scope.project = project;
                $scope.load = function (projectId) {
                    Project.get({projectId: projectId}, function(result) {
                        $scope.project = result;
                    });
                };
/*
                var unsubscribe = $rootScope.$on('lighthouseApp:projectUpdate', function(event, result) {
                    $scope.project = result;
                });
                $scope.$on('$destroy', unsubscribe);
*/
            });
