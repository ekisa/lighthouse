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
                url: 'project/{projectId}/scan/{id}',
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
                    project: ['$stateParams', 'Project', function($stateParams, Project) {
                        return Project.get({id : $stateParams.projectId});
                    }]
                }
            })
            */
            .state('scan.new', {
                parent: 'scan',
                url: 'project/{projectId}/newScan',
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
                url: 'project/{projectId}/editScan/{scanId}',
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
                                return Scan.get({projectId: $stateParams.projectId, scanId : $stateParams.scanId});
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
                url: 'project/{projectId}/deleteScan/{scanId}',
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
                                return Scan.get({projectId: $stateParams.projectId, scanId : $stateParams.scanId});
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
