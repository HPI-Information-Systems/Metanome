'use strict';

angular.module('Metanome')
  .factory('Parameter', ['$resource',
    function ($resource, $window) {
      var url = $window.location.href.split('#')[0];
      return $resource(url + 'api/parameter/:algorithm/:what', {}, {
        get: {
          method: 'GET',
          params: {
            algorithm: '@algorithm',
            what: ''
          }, isArray: true
        },
        authorsDescription: {
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
