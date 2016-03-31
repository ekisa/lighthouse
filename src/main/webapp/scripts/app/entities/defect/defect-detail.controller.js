'use strict';

angular.module('lighthouseApp')
    .controller('DefectDetailController', function ($scope, $rootScope, $stateParams, defect, Defect) {
        $scope.defect = defect;
        $scope.load = function (defectId) {
            Defect.get({defectId: defectId, scanId: $scope.scan.id}, function(result) {
                $scope.defect = result;
            });
        };
        var unsubscribe = $rootScope.$on('lighthouseApp:defectUpdate', function(event, result) {
            $scope.defect = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
