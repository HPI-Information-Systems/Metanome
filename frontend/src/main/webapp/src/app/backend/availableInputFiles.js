'use strict';

angular.module('Metanome')
  .factory('AvailableInputFiles', ['$resource', 'EnvironmentConfig',
    function ($resource, EnvironmentConfig) {
      return $resource(EnvironmentConfig.API + '/api/file-inputs/:operation', {}, {
        get: {
          method: 'GET',
          params: {
            operation: 'available-input-files'
            },
          isArray: true
        },
        getDirectory: {
          method: 'POST',
          params: {
            operation: 'get-directory-files'
          },
          isArray: true
        }
      });
    }
  ])

;
