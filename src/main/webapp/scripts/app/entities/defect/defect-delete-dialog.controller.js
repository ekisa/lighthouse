'use strict';

angular.module('lighthouseApp')
	.controller('DefectDeleteController', function($scope, $uibModalInstance, entity, Defect) {

        $scope.defect = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (defectId) {
            Defect.delete({id: defectId},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
