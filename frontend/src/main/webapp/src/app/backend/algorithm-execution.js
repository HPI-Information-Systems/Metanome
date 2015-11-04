'use strict';

angular.module('Metanome')
  .factory('AlgorithmExecution', ['$resource',
      function ($resource) {
          return $resource('http://127.0.0.1:8888/api/algorithm-execution', {}, {
              run: {
                  method: 'POST'
              }
          });
      }
  ])

;
