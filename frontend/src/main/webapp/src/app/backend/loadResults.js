'use strict';

angular.module('Metanome')
  .factory('LoadResults', ['$resource',
    function ($resource, $window) {
      var url = $window.location.href.split('#')[0];
      return $resource(url + 'api/result-store/:type/:id/:notDetailed', {}, {
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
