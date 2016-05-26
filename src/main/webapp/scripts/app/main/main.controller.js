'use strict';

angular.module('lighthouseApp')
    .controller('MainController', function ($scope, $state, Principal, PluginHomeDTO, ParseLinks) {
        Principal.identity().then(function(account) {
            $scope.account = account;
            $scope.isAuthenticated = Principal.isAuthenticated;
        });

        $scope.plugins = [];
        $scope.predicate = 'severityOrder';
        $scope.reverse = false;
        $scope.page = 1;
        $scope.loadPluginHomeDTOs = function() {
            PluginHomeDTO.query({page: $scope.page - 1, size: 10, sort: [$scope.predicate + ',' + ($scope.reverse ? 'asc' : 'desc'), 'severityOrder']}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.totalItems = headers('X-Total-Count');
                $scope.plugins = result;
            });
        };

        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadPluginHomeDTOs();
        };

        $scope.loadPluginHomeDTOs();

    });
