'use strict';

angular.module('Metanome')
  .factory('Results', ['$resource',
    function ($resource, $window) {
      var url = $window.location.href.split('#')[0];
      return $resource(url + 'api/result-store/:method/:type/:sort/:ascending/:from/:to', {}, {
        get: {
          method: 'GET',
          params: {
            type: '@type',
            sort: '@sort',
            from: '@from',
            to: '@to',
            method: 'get-from-to',
            ascending: 'true'
          }, isArray: true
        }
      });
    }
  ])

;
