'use strict';

angular.module('Metanome')
  .factory('CountResults', ['$resource',
    function ($resource, $window) {
      var url = $window.location.href.split('#')[0];
      return $resource(url + 'api/result-store/count/:type', {}, {
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
