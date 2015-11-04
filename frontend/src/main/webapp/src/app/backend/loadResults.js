'use strict';

angular.module('Metanome')
  .factory('LoadResults', ['$resource',
      function ($resource) {
          return $resource('http://127.0.0.1:8888/api/result-store/:type/:id/:notDetailed', {}, {
              load: {
                  method: 'POST',
                  params: {
                      id: '@id',
                      notDetailed: '@notDetailed',
                      type: 'load-execution'
                  },
                  isArray: true
              },
              file: {
                  method: 'GET',
                  params: {
                      id: '@id',
                      notDetailed: '@notDetailed',
                      type: 'load-results'
                  },
                  isArray: true
              }
          });
      }
  ])

;
