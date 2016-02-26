'use strict';

angular.module('lighthouseApp')
    .factory('Register', function ($resource) {
        return $resource('api/register', {}, {
        });
    });


