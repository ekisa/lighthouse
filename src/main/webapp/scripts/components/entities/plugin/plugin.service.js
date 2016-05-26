'use strict';

angular.module('lighthouseApp')
    .factory('Plugin', function ($resource, DateUtils) {
        return $resource('api/plugins/:pluginId', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': { method:'PUT' },
            'delete': { method:'DELETE'}
        });
    })
.factory('PluginHomeDTO', function ($resource) {
    return $resource('api/plugin-last-scan-defects-count-grouped-by-severity', {}, {
        'query': { method: 'GET', isArray: true}
    });
});
