'use strict';

angular.module('lighthouseApp')
    .controller('PluginDetailController',
            function ($scope, $rootScope, $stateParams, plugin, Plugin, ParseLinks) {
                $scope.plugin = plugin;
                $scope.load = function (pluginId) {
                    Plugin.get({pluginId: pluginId}, function(result) {
                        $scope.plugin = result;
                    });
                };
/*
                var unsubscribe = $rootScope.$on('lighthouseApp:pluginUpdate', function(event, result) {
                    $scope.plugin = result;
                });
                $scope.$on('$destroy', unsubscribe);
*/
            });
