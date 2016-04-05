'use strict';

angular.module('lighthouseApp').controller('PluginDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'plugin', 'Plugin',
        function($scope, $stateParams, $uibModalInstance, plugin, Plugin) {

        $scope.plugin = plugin;

        $scope.load = function(pluginId) {
            Plugin.get({pluginId : pluginId}, function(result) {
                $scope.plugin = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('lighthouseApp:pluginUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.plugin.id != null) {
                Plugin.update($scope.plugin, onSaveSuccess, onSaveError);
            } else {
                Plugin.save($scope.plugin, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
