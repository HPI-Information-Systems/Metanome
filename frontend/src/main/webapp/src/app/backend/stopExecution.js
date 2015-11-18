'use strict';

angular.module('Metanome')
    .factory('StopExecution', ['$resource',
        function ($resource) {
            return $resource('http://127.0.0.1:8888/api/algorithm-execution/stop/:identifier', {}, {
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
