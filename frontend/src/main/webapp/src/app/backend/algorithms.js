'use strict';

angular.module('Metanome')
  .factory('Algorithms', ['$resource',
      function ($resource) {
          return $resource('http://127.0.0.1:8888/api/algorithms/:type', {}, {
              get: {
                  method: 'GET',
                  params: {
                    type: '@type'
                  }, isArray: true
              }
          });
      }
  ])

;
