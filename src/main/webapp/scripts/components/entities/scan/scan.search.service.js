'use strict';

angular.module('lighthouseApp')
    .factory('ScanSearch', function ($resource) {
        return $resource('api/projects/:projectId/_search/scans/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
