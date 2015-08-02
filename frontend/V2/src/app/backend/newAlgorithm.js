'use strict';

angular.module('v2')
    .factory('NewAlgorithm', ['$resource',
        function ($resource) {
            return $resource('http://127.0.0.1:8888/api/algorithms/store', {}, {
                store: {
                    method: 'POST'
                }
            });
        }
    ])

; 
