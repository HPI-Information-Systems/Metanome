'use strict';

angular.module('Metanome')
  .factory('Execution', ['$resource', 'EnvironmentConfig',
    function ($resource, EnvironmentConfig) {
      return $resource(EnvironmentConfig.API + '/api/executions/get/:id', {}, {
        get: {
          method: 'GET',
          params: {
            id: '@id'
          }
        }
      });
    }
  ])
;
