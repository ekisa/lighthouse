'use strict';

angular.module('lighthouseApp')
    .controller('DefectController', function ($scope, $state, $stateParams, scan, Defect, ParseLinks) {
        $scope.scan = scan;
        $scope.defects = [];
        $scope.predicate = 'id';
        $scope.reverse = true;
        $scope.page = 1;

        $scope.loadAllDefects = function() {
            Defect.query({scanId: $stateParams.scanId, page: $scope.page - 1, size: 20, sort: [$scope.predicate + ',' + ($scope.reverse ? 'asc' : 'desc'), 'id']}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.totalItems = headers('X-Total-Count');
                $scope.defects = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAllDefects();
        };
        $scope.loadAllDefects();

        $scope.refresh = function () {
            $scope.loadAllDefects();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.defect = null;
        };

    });
