'use strict';

angular.module('Metanome')
  .factory('AvailableInputFiles', ['$resource',
    function ($resource) {
      return $resource('http://127.0.0.1:8888/api/file-inputs/available-input-files', {}, {
        get: {
          method: 'GET',
          params: {},
          isArray: true
        }
      });
    }
  ])

;
