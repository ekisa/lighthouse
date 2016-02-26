'use strict';

angular.module('lighthouseApp')
    .factory('Defect', function ($resource, DateUtils) {
        return $resource('api/defects/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });
