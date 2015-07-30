'use strict';

angular.module('v2')
  .factory('AlgorithmResults', ['$resource',
      function ($resource) {
          return $resource('http://127.0.0.1:8888/api/result-store/get-from-to/:type/:sort/true/:from/:to', {}, {
              get: {
                  method: 'GET',
                  params: {
                    type: '@type',
                    sort: '@sort',
                    from: '@from',
                    to: '@to'
                  }, isArray: true
              }
          });
      }
  ])

; 
