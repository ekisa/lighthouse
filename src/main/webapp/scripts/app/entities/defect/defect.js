'use strict';

angular.module('lighthouseApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('defect', {
                parent: 'entity',
                url: '/defects',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'lighthouseApp.defect.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/defect/defects.html',
                        controller: 'DefectController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('defect');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('defect.detail', {
                parent: 'entity',
                url: '/defect/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'lighthouseApp.defect.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/defect/defect-detail.html',
                        controller: 'DefectDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('defect');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Defect', function($stateParams, Defect) {
                        return Defect.get({id : $stateParams.id});
                    }]
                }
            })
            .state('defect.new', {
                parent: 'defect',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/defect/defect-dialog.html',
                        controller: 'DefectDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    title: null,
                                    explanation: null,
                                    code: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('defect', null, { reload: true });
                    }, function() {
                        $state.go('defect');
                    })
                }]
            })
            .state('defect.edit', {
                parent: 'defect',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/defect/defect-dialog.html',
                        controller: 'DefectDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Defect', function(Defect) {
                                return Defect.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('defect', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('defect.delete', {
                parent: 'defect',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/defect/defect-delete-dialog.html',
                        controller: 'DefectDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Defect', function(Defect) {
                                return Defect.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('defect', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
