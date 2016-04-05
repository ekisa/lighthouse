'use strict';

angular.module('lighthouseApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('scan', {
                parent: 'entity',
                url: '/scan',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'lighthouseApp.scan.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/scan/scans.html',
                        controller: 'ScanController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('scan');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            /*
            .state('scan.detail', {
                parent: 'entity',
                url: 'plugin/{pluginId}/scan/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'lighthouseApp.scan.detail.title'
                },
                views: {
                    'scanDetails@': {
                        templateUrl: 'scripts/app/entities/scan/scan-detail.html',
                        controller: 'ScanDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('scan');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Scan', function($stateParams, Scan) {
                        return Scan.get({id : $stateParams.scanId});
                    }],
                    plugin: ['$stateParams', 'Plugin', function($stateParams, Plugin) {
                        return Plugin.get({id : $stateParams.pluginId});
                    }]
                }
            })
            */
            .state('scan.new', {
                parent: 'scan',
                url: 'plugin/{pluginId}/newScan',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/scan/scan-dialog.html',
                        controller: 'ScanDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    name: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('scan', null, { reload: true });
                    }, function() {
                        $state.go('scan');
                    })
                }]
            })
            .state('scan.edit', {
                parent: 'scan',
                url: 'plugin/{pluginId}/editScan/{scanId}',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/scan/scan-dialog.html',
                        controller: 'ScanDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Scan', function(Scan) {
                                return Scan.get({pluginId: $stateParams.pluginId, scanId : $stateParams.scanId});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('scan', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('scan.delete', {
                parent: 'scan',
                url: 'plugin/{pluginId}/deleteScan/{scanId}',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/scan/scan-delete-dialog.html',
                        controller: 'ScanDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Scan', function(Scan) {
                                return Scan.get({pluginId: $stateParams.pluginId, scanId : $stateParams.scanId});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('scan', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
