'use strict';

angular.module('lighthouseApp')
    .controller('ScanController', function ($scope, $state, $stateParams, project, Project, Scan, ParseLinks) {
        $scope.project = project;
        //$scope.scans = scans;
        $scope.predicate = 'id';
        $scope.reverse = true;
        $scope.page = 1;

        /*
        $scope.load = function (projectId) {
            Project.get({id: projectId}, function(result) {
                $scope.project = result;
            });
        };

        $scope.load($stateParams.projectId);
        */

        $scope.loadAllScans = function() {
            Scan.query({projectId: $stateParams.projectId, page: $scope.page - 1, size: 20, sort: [$scope.predicate + ',' + ($scope.reverse ? 'asc' : 'desc'), 'id']}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.totalItems = headers('X-Total-Count');
                $scope.scans = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAllScans();
        };
        $scope.loadAllScans();

        $scope.refresh = function () {
            $scope.loadAllScans();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.scan = null;
        };

    });
