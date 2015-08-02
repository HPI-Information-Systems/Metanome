'use strict';

angular.module('v2')
  .factory('LoadResults', ['$resource',
      function ($resource) {
          return $resource('http://127.0.0.1:8888/api/result-store/load-execution/:id/:detailed', {}, {
              load: {
                  method: 'GET',
                  params: {
                    id: '@id',
                    detailed: '@detailed'
                  },
                  isArray: true
              }
          });
      }
  ])

; 
