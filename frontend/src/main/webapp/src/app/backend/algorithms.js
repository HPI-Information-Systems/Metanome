'use strict';

angular.module('Metanome')
  .factory('Algorithms', ['$resource',
    function ($resource) {
      var url = window.location.href.split('#')[0];
      return $resource(url + 'api/algorithms/:type', {}, {
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
