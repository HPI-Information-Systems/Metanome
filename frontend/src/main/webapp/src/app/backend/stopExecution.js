'use strict';

angular.module('Metanome')
  .factory('StopExecution', ['$resource',
    function ($resource) {
      var url = window.location.href.split('#')[0];
      return $resource(url + 'api/algorithm-execution/stop/:identifier', {}, {
        stop: {
          method: 'POST',
          params: {
            identifier: '@identifier'
          }
        }
      });
    }
  ])

;
