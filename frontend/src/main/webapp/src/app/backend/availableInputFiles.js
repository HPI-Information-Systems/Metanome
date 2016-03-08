'use strict';

angular.module('Metanome')
  .factory('AvailableInputFiles', ['$resource', 'EnvironmentConfig',
    function ($resource, EnvironmentConfig) {
      return $resource(EnvironmentConfig.API + '/api/file-inputs/available-input-files', {}, {
        get: {
          method: 'GET',
          params: {},
          isArray: true
        }
      });
    }
  ])

;
