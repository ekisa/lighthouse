'use strict';

angular.module('lighthouseApp')
    .controller('ProjectDetailController', function ($scope, $rootScope, $stateParams, entity, Project, Defect) {
        $scope.project = entity;
        $scope.load = function (id) {
            Project.get({id: id}, function(result) {
                $scope.project = result;
            });
        };
        var unsubscribe = $rootScope.$on('lighthouseApp:projectUpdate', function(event, result) {
            $scope.project = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
