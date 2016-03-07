'use strict';

angular.module('Metanome')
  .factory('AvailableAlgorithmFiles', ['$resource',
    function ($resource) {
      var url = window.location.href.split('#')[0];
      return $resource(url + 'api/algorithms/available-algorithm-files', {}, {
        get: {
          method: 'GET',
          params: {
            type: '@type'
          },
          isArray: true
        }
      });
    }
  ])

;
