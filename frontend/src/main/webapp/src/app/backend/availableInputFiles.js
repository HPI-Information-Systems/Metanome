'use strict';

angular.module('Metanome')
  .factory('AvailableInputFiles', ['$resource', 'EnvironmentConfig',
    function ($resource, EnvironmentConfig) {
      return $resource(EnvironmentConfig.API + '/api/file-inputs/:operation/:name', {}, {
        get: {
          method: 'GET',
          params: {
            operation: 'available-input-files'
            },
          isArray: true
        },
        getDirectory: {
          method: 'GET',
          params: {
            operation: 'get-directory-files',
            name: '@name'
          },
          isArray: true
        }
      });
    }
  ])

;
