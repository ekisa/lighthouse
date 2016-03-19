'use strict';

angular.module('lighthouseApp')
    .controller('ScanController', function ($scope, $state, Scan, ParseLinks) {
        $scope.scans = [];
        $scope.predicate = 'id';
        $scope.reverse = true;
        $scope.page = 1;
        $scope.loadAllScans = function() {
            Scan.query({page: $scope.page - 1, size: 20, sort: [$scope.predicate + ',' + ($scope.reverse ? 'asc' : 'desc'), 'id']}, function(result, headers) {
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