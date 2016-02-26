'use strict';

angular.module('lighthouseApp')
    .controller('DefectDetailController', function ($scope, $rootScope, $stateParams, entity, Defect, Project) {
        $scope.defect = entity;
        $scope.load = function (id) {
            Defect.get({id: id}, function(result) {
                $scope.defect = result;
            });
        };
        var unsubscribe = $rootScope.$on('lighthouseApp:defectUpdate', function(event, result) {
            $scope.defect = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
