'use strict';

angular.module('v2')
  .factory('LoadResults', ['$resource',
      function ($resource) {
          return $resource('http://127.0.0.1:8888/api/result-store/:type/:id/:detailed', {}, {
              load: {
                  method: 'GET',
                  params: {
                      id: '@id',
                      detailed: '@detailed',
                      type: 'load-execution'
                  },
                  isArray: true
              },
              file: {
                  method: 'GET',
                  params: {
                      id: '@id',
                      detailed: '@detailed',
                      type: 'load-results'
                  },
                  isArray: true
              }
          });
      }
  ])

; 
