'use strict';

angular.module('Metanome')
  .factory('Execution', ['$resource',
    function ($resource) {
      var url = window.location.href.split('#')[0];
      return $resource(url + 'api/executions/get/:id', {}, {
        get: {
          method: 'GET',
          params: {
            id: '@id'
          }
        }
      });
    }
  ])
;
