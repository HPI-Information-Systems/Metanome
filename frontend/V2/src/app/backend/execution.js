'use strict';

angular.module('v2')
    .factory('Execution', ['$resource',
        function ($resource) {
            return $resource('http://127.0.0.1:8888/api/executions/get/:id', {}, {
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
