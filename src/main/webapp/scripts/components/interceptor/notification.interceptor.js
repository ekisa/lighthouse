 'use strict';

angular.module('lighthouseApp')
    .factory('notificationInterceptor', function ($q, AlertService) {
        return {
            response: function(response) {
                var alertKey = response.headers('X-lighthouseApp-alert');
                if (angular.isString(alertKey)) {
                    AlertService.success(alertKey, { param : response.headers('X-lighthouseApp-params')});
                }
                return response;
            }
        };
    });
