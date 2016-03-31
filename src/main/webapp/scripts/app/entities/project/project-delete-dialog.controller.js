'use strict';

angular.module('lighthouseApp')
	.controller('ProjectDeleteController', function($scope, $uibModalInstance, project, Project) {

        $scope.project = project;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (projectId) {
            Project.delete({projectId: projectId},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
