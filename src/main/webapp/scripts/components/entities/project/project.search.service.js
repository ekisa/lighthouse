'use strict';

angular.module('lighthouseApp')
    .factory('ProjectSearch', function ($resource) {
        return $resource('api/_search/projects/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
