'use strict';

angular.module('Metanome')
  .factory('CountResults', ['$resource', 'EnvironmentConfig',
    function ($resource, EnvironmentConfig) {
      return $resource(EnvironmentConfig.API + '/api/result-store/count/:type', {}, {
        get: {
          method: 'GET',
          params: {
            type: '@type'
          }
        }
      });
    }
  ])

;
