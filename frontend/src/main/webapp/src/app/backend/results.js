'use strict';

angular.module('Metanome')
  .factory('Results', ['$resource',
      function ($resource) {
          return $resource('http://127.0.0.1:8888/api/result-store/:method/:type/:sort/:ascending/:from/:to', {}, {
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
