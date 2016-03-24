'use strict';

angular.module('lighthouseApp').controller('ProjectDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'project', 'Project', 'Defect',
        function($scope, $stateParams, $uibModalInstance, project, Project, Defect) {

        $scope.project = project;
        $scope.defects = Defect.query();
        $scope.load = function(projectId) {
            Project.get({projectId : projectId}, function(result) {
                $scope.project = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('lighthouseApp:projectUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.project.id != null) {
                Project.update($scope.project, onSaveSuccess, onSaveError);
            } else {
                Project.save($scope.project, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
