'use strict';
angular.module('lighthouseApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('project', {
                parent: 'entity',
                url: '/projects',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'lighthouseApp.project.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/project/projects.html',
                        controller: 'ProjectController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('project');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('project.detail', {
                parent: 'entity',
                url: '/project/{projectId}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'lighthouseApp.project.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/project/project-detail.html',
                        controller: 'ProjectDetailController'
                    },
                    'project.detail.scans@project.detail': {
                        templateUrl: 'scripts/app/entities/scan/scans.html',
                        controller: 'ScanController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('project');
                        return $translate.refresh();
                    }],
                    project: ['$stateParams', 'Project', function($stateParams, Project) {
                        return Project.get({projectId : $stateParams.projectId});
                    }]
                }
            })
            .state('project.detail.defects', {
                parent : 'project.detail',
                url: '/scan/{scanId}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'lighthouseApp.scan.detail.title'
                },
                views: {
                    'project.detail.scan.defects@project.detail': {
                        templateUrl: 'scripts/app/entities/defect/defects.html',
                        controller: 'DefectController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('scan');
                        $translatePartialLoader.addPart('defect');
                        return $translate.refresh();
                    }],
                    scan :  ['$stateParams', 'Scan', function($stateParams, Scan) {
                        return Scan.get({scanId : $stateParams.scanId, projectId: $stateParams.projectId});
                    }]
                }
            })
            .state('project.detail.defects.detail', {
                parent : 'project.detail.defects',
                url: '/defect/{defectId}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'lighthouseApp.defect.detail.title'
                },
                views: {
                    'project.detail.scan.defects.detail@project.detail': {
                        templateUrl: 'scripts/app/entities/defect/defect-detail.html',
                        controller: 'DefectDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('defect');
                        return $translate.refresh();
                    }],
                    defect: ['$stateParams', 'Defect', function($stateParams, Defect) {
                        return Defect.get({defectId : $stateParams.defectId});
                    }]
                }
            })
            .state('project.new', {
                parent: 'project',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/project/project-dialog.html',
                        controller: 'ProjectDialogController',
                        size: 'lg',
                        resolve: {
                            project: function () {
                                return {
                                    name: null,
                                    projectId: null
                                };
                            },
                            projectId : null
                        }
                    }).result.then(function(result) {
                        $state.go('project', null, { reload: true });
                    }, function() {
                        $state.go('project');
                    })
                }]
            })
            .state('project.edit', {
                parent: 'project',
                url: '/{projectId}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/project/project-dialog.html',
                        controller: 'ProjectDialogController',
                        size: 'lg',
                        resolve: {
                            project: ['Project', function(Project) {
                                return Project.get({projectId : $stateParams.projectId});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('project', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('project.delete', {
                parent: 'project',
                url: '/{projectId}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/project/project-delete-dialog.html',
                        controller: 'ProjectDeleteController',
                        size: 'md',
                        resolve: {
                            project: ['Project', function(Project) {
                                return Project.get({projectId : $stateParams.projectId});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('project', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
