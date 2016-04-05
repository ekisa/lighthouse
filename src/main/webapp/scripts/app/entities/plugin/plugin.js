'use strict';
angular.module('lighthouseApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('plugin', {
                parent: 'entity',
                url: '/plugins',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'lighthouseApp.plugin.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/plugin/plugins.html',
                        controller: 'PluginController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('plugin');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('plugin.detail', {
                parent: 'entity',
                url: '/plugin/{pluginId}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'lighthouseApp.plugin.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/plugin/plugin-detail.html',
                        controller: 'PluginDetailController'
                    },
                    'plugin.detail.scans@plugin.detail': {
                        templateUrl: 'scripts/app/entities/scan/scans.html',
                        controller: 'ScanController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('plugin');
                        return $translate.refresh();
                    }],
                    plugin: ['$stateParams', 'Plugin', function($stateParams, Plugin) {
                        return Plugin.get({pluginId : $stateParams.pluginId});
                    }]
                }
            })
            .state('plugin.detail.defects', {
                parent : 'plugin.detail',
                url: '/scan/{scanId}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'lighthouseApp.scan.detail.title'
                },
                views: {
                    'plugin.detail.scan.defects@plugin.detail': {
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
                        return Scan.get({scanId : $stateParams.scanId, pluginId: $stateParams.pluginId});
                    }]
                }
            })
            .state('plugin.detail.defects.detail', {
                parent : 'plugin.detail.defects',
                url: '/defect/{defectId}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'lighthouseApp.defect.detail.title'
                },
                views: {
                    'plugin.detail.scan.defects.detail@plugin.detail': {
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
            .state('plugin.new', {
                parent: 'plugin',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/plugin/plugin-dialog.html',
                        controller: 'PluginDialogController',
                        size: 'lg',
                        resolve: {
                            plugin: function () {
                                return {
                                    name: null,
                                    pluginId: null
                                };
                            },
                            pluginId : null
                        }
                    }).result.then(function(result) {
                        $state.go('plugin', null, { reload: true });
                    }, function() {
                        $state.go('plugin');
                    })
                }]
            })
            .state('plugin.edit', {
                parent: 'plugin',
                url: '/{pluginId}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/plugin/plugin-dialog.html',
                        controller: 'PluginDialogController',
                        size: 'lg',
                        resolve: {
                            plugin: ['Plugin', function(Plugin) {
                                return Plugin.get({pluginId : $stateParams.pluginId});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('plugin', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('plugin.delete', {
                parent: 'plugin',
                url: '/{pluginId}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/plugin/plugin-delete-dialog.html',
                        controller: 'PluginDeleteController',
                        size: 'md',
                        resolve: {
                            plugin: ['Plugin', function(Plugin) {
                                return Plugin.get({pluginId : $stateParams.pluginId});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('plugin', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
