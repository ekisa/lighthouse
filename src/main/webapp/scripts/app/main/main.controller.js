'use strict';

angular.module('lighthouseApp')
    .controller('MainController', function ($scope, $state, Principal, Plugin, ParseLinks) {
        Principal.identity().then(function(account) {
            $scope.account = account;
            $scope.isAuthenticated = Principal.isAuthenticated;
        });

        $scope.plugins = [];
        $scope.predicate = 'id';
        $scope.reverse = true;
        $scope.page = 1;
        $scope.loadAllPlugins = function() {
            Plugin.query({page: $scope.page - 1, size: 10, sort: [$scope.predicate + ',' + ($scope.reverse ? 'asc' : 'desc'), 'id']}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.totalItems = headers('X-Total-Count');
                $scope.plugins = result;
            });
        };

        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAllPlugins();
        };

        $scope.loadAllPlugins();

    });
