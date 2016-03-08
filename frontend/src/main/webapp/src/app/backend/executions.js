'use strict';

angular.module('Metanome')
  .factory('Executions', ['$resource', 'EnvironmentConfig',
    function ($resource, EnvironmentConfig) {
      return $resource(EnvironmentConfig.API + '/api/executions', {}, {
        getAll: {
          method: 'GET',
          isArray: true
        }
      });
    }
  ])

;
