'use strict';

angular.module('Metanome')
  .factory('StopExecution', ['$resource', 'EnvironmentConfig',
    function ($resource, EnvironmentConfig) {
      return $resource(EnvironmentConfig.API + '/api/algorithm-execution/stop/:identifier', {}, {
        stop: {
          method: 'POST',
          params: {
            identifier: '@identifier'
          }
        }
      });
    }
  ])

;
