'use strict';

angular.module('lighthouseApp')
    .factory('PluginSearch', function ($resource) {
        return $resource('api/_search/plugins/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
