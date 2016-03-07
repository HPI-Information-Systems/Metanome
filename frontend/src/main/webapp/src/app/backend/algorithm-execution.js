'use strict';

angular.module('Metanome')
  .factory('AlgorithmExecution', ['$resource',
    function ($resource) {
      var url = window.location.href.split('#')[0];
      return $resource(url + 'api/algorithm-execution', {}, {
        run: {
          method: 'POST'
        }
      });
    }
  ])

;
