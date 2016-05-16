'use strict';

angular.module('lighthouseApp')
    .factory('DefectSearch', function ($resource) {
        return $resource('api/_search/defects', {}, {
            'query': { method: 'POST', isArray: true}
        });
    });
