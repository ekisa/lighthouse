'use strict';

angular.module('lighthouseApp')
    .controller('DefectDetailController', function ($scope, $rootScope, $stateParams, defect, scan, Defect) {
        $scope.scan = scan;
        $scope.defect = defect;
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
