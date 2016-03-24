'use strict';

angular.module('lighthouseApp')
    .factory('Scan', function ($resource, DateUtils) {
        return $resource('api/projects/:projectId/scans/:scanId', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'save': { method:'POST' },
            'update': { method:'PUT' },
            'delete':{ method:'DELETE'}
        });
    });
