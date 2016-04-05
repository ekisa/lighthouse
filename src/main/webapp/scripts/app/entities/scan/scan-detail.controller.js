'use strict';

angular.module('lighthouseApp')
    .controller('ScanDetailController', function ($scope, $rootScope, $stateParams, entity, plugin, Scan, Defect) {
        $scope.scan = entity;
        $scope.load = function (pluginId, scanId) {
            Scan.get({pluginId: pluginId, scanId: scanId}, function(result) {
                $scope.scan = result;
            });
        };
        var unsubscribe = $rootScope.$on('lighthouseApp:scanUpdate', function(event, result) {
            $scope.scan = result;
        });
        $scope.$on('$destroy', unsubscribe);
    });
