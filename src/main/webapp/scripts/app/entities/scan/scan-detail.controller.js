'use strict';

angular.module('lighthouseApp')
    .controller('ScanDetailController', function ($scope, $rootScope, $stateParams, entity, project, Scan, Defect) {
        $scope.scan = entity;
        $scope.load = function (id) {
            Scan.get({id: id}, function(result) {
                $scope.scan = result;
            });
        };
        var unsubscribe = $rootScope.$on('lighthouseApp:scanUpdate', function(event, result) {
            $scope.scan = result;
        });
        $scope.$on('$destroy', unsubscribe);
    });
