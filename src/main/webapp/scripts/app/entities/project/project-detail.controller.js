'use strict';

angular.module('lighthouseApp')
    .controller('ProjectDetailController',
            function ($scope, $rootScope, $stateParams, entity, Project, Scan, ParseLinks) {
                $scope.project = entity;

                $scope.load = function (id) {
                    Project.get({id: id}, function(result) {
                        $scope.project = result;
                    });
                };

                var unsubscribe = $rootScope.$on('lighthouseApp:projectUpdate', function(event, result) {
                    $scope.project = result;
                });
                $scope.$on('$destroy', unsubscribe);

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

                $scope.loadAllScans();
            });
