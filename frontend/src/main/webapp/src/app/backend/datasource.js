'use strict';

angular.module('Metanome')
  .factory('Datasource', ['$resource', 'EnvironmentConfig',
    function ($resource, EnvironmentConfig) {
      return $resource(EnvironmentConfig.API + '/api/:type', {}, {
        get: {
          method: 'GET',
          params: {
            type: '@type'
          },
          isArray: true
        }
      });
    }
  ])

;
