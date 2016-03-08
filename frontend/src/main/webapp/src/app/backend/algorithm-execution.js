'use strict';

angular.module('Metanome')
  .factory('AlgorithmExecution', ['$resource', 'EnvironmentConfig',
    function ($resource, EnvironmentConfig) {
      return $resource(EnvironmentConfig.API + '/api/algorithm-execution', {}, {
        run: {
          method: 'POST'
        }
      });
    }
  ])

;
