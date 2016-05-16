'use strict';

angular.module('lighthouseApp')
    .factory('Defect', function ($resource, DateUtils) {
        return $resource('api/defects/:defectId', {scanId:'@scan.id'}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': { method:'PUT' },
            'save': { method:'POST' },
            'find': { method:'POST' }
        });
    });
