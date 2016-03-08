'use strict';

angular.module('Metanome')
  .factory('Results', ['$resource', 'EnvironmentConfig',
    function ($resource, EnvironmentConfig) {
      return $resource(EnvironmentConfig.API + '/api/result-store/:method/:type/:sort/:ascending/:from/:to', {}, {
        get: {
          method: 'GET',
          params: {
            type: '@type',
            sort: '@sort',
            from: '@from',
            to: '@to',
            method: 'get-from-to',
            ascending: 'true'
          }, isArray: true
        }
      });
    }
  ])

;
