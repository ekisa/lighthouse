'use strict';

angular.module('lighthouseApp')
    .factory('ScanSearch', function ($resource) {
        return $resource('api/_search/scans/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
