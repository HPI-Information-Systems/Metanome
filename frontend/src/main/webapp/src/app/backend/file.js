'use strict';

angular.module('Metanome')
  .factory('File', ['$resource',
    function ($resource) {
      var url = window.location.href.split('#')[0];
      return $resource(url + 'api/file-inputs/get/:id', {}, {
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
