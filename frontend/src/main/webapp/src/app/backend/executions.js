'use strict';

angular.module('Metanome')
  .factory('Executions', ['$resource',
    function ($resource) {
      var url = window.location.href.split('#')[0];
      return $resource(url + 'api/executions', {}, {
        getAll: {
          method: 'GET',
          isArray: true
        }
      });
    }
  ])

;
