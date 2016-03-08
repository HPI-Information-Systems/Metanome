'use strict';

angular.module('Metanome')
  .factory('Parameter', ['$resource', 'EnvironmentConfig',
    function ($resource, EnvironmentConfig) {
      return $resource(EnvironmentConfig.API + '/api/parameter/:algorithm/:what', {}, {
        get: {
          method: 'GET',
          params: {
            algorithm: '@algorithm',
            what: ''
          }, isArray: true
        },
        authorsDescription: {
          method: 'GET',
          params: {
            algorithm: '@algorithm',
            what: 'authors-description'
          }
        }
      });
    }
  ])

;
