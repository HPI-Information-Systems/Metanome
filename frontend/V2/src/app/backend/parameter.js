'use strict';

angular.module('v2')
  .factory('Parameter', ['$resource',
      function ($resource) {
          return $resource('http://127.0.0.1:8888/api/parameter/:algorithm', {}, {
              get: {
                  method: 'GET',
                  params: {
                    algorithm: '@algorithm'
                  }, isArray: true
              }
          });
      }
  ])

; 
