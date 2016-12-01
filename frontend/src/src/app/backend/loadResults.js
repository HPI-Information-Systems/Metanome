'use strict';

angular.module('Metanome')
  .factory('LoadResults', ['$resource', 'EnvironmentConfig',
    function ($resource, EnvironmentConfig) {
      return $resource(EnvironmentConfig.API + '/api/result-store/:type/:id/:notDetailed', {}, {
        load: {
          method: 'POST',
          params: {
            id: '@id',
            notDetailed: '@notDetailed',
            type: 'load-execution'
          },
          isArray: true
        },
        file: {
          method: 'GET',
          params: {
            id: '@id',
            notDetailed: '@notDetailed',
            type: 'load-results'
          },
          isArray: true
        }
      });
    }
  ])

;
