'use strict';

angular.module('Metanome')
  .factory('File', ['$resource', 'EnvironmentConfig',
    function ($resource, EnvironmentConfig) {
      return $resource(EnvironmentConfig.API + '/api/file-inputs/get/:id', {}, {
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
