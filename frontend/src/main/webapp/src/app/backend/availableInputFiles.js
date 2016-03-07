'use strict';

angular.module('Metanome')
  .factory('AvailableInputFiles', ['$resource',
    function ($resource) {
      var url = window.location.href.split('#')[0];
      return $resource(url + 'api/file-inputs/available-input-files', {}, {
        get: {
          method: 'GET',
          params: {},
          isArray: true
        }
      });
    }
  ])

;
