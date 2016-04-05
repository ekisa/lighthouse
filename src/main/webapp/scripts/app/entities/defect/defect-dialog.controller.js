'use strict';

angular.module('lighthouseApp').controller('DefectDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Defect', 'Plugin',
        function($scope, $stateParams, $uibModalInstance, entity, Defect, Plugin) {

        $scope.defect = entity;
        $scope.plugins = Plugin.query();
        $scope.load = function(defectId) {
            Defect.get({id : defectId}, function(result) {
                $scope.defect = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('lighthouseApp:defectUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.defect.id != null) {
                Defect.update($scope.defect, onSaveSuccess, onSaveError);
            } else {
                Defect.save($scope.defect, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
