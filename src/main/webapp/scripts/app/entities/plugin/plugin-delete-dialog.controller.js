'use strict';

angular.module('lighthouseApp')
	.controller('PluginDeleteController', function($scope, $uibModalInstance, plugin, Plugin) {

        $scope.plugin = plugin;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (pluginId) {
            Plugin.delete({pluginId: pluginId},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
