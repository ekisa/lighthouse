'use strict';

angular.module('lighthouseApp')
	.controller('DefectDeleteController', function($scope, $uibModalInstance, entity, Defect) {

        $scope.defect = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Defect.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
