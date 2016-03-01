'use strict';

angular.module('Metanome')
  .factory('Parameter', ['$resource',
    function ($resource) {
      return $resource('http://127.0.0.1:8888/api/parameter/:algorithm/:what', {}, {
        get: {
          method: 'GET',
          params: {
            algorithm: '@algorithm',
            what: ''
          }, isArray: true
        },
        authors_description: {
          method: 'GET',
          params: {
            algorithm: '@algorithm',
            what: 'authors-description'
          }
        }
      });
    }
  ])

;
