'use strict';

angular.module('lighthouseApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('home', {
                parent: 'site',
                url: '/',
                data: {
                    authorities: []
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/main/main.html',
                        controller: 'MainController'
                    }
                },
                resolve: {
                    mainTranslatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate,$translatePartialLoader) {
                        $translatePartialLoader.addPart('main');
                        $translatePartialLoader.addPart('plugin')
                        return $translate.refresh();
                    }]
                }
            })
            /*
            .state('home.plugin', {
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
            .state('home.plugin.detail', {
                parent: 'entity',
                url: '/plugin/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'lighthouseApp.plugin.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/plugin/plugin-detail.html',
                        controller: 'PluginDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('plugin');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Plugin', function($stateParams, Plugin) {
                        return Plugin.get({id : $stateParams.pluginId});
                    }]
                }
            });*/
    });
