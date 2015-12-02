'use strict';

angular.module('Metanome')
  .factory('CountResults', ['$resource',
    function ($resource) {
      return $resource('http://127.0.0.1:8888/api/result-store/count/:type', {}, {
        get: {
          method: 'GET',
          params: {
            type: '@type'
          }
        }
      });
    }
  ])

;
