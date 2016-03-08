'use strict';

angular.module('Metanome')
  .factory('Algorithms', ['$resource', 'EnvironmentConfig',
    function ($resource, EnvironmentConfig) {
      return $resource(EnvironmentConfig.API + '/api/algorithms/:type', {}, {
        get: {
          method: 'GET',
          params: {
            type: '@type'
          }, isArray: true
        }
      });
    }
  ])

;
