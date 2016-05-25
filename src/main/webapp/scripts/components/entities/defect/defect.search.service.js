'use strict';

angular.module('lighthouseApp')
    .factory('DefectSearch', function ($resource) {
        return $resource('api/_search/defects/:scanId', {}, {
            'query': { method: 'GET', isArray: true,
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }}
        });
    });
