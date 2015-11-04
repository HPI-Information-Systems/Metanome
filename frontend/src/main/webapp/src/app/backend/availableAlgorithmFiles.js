'use strict';

angular.module('Metanome')
    .factory('AvailableAlgorithmFiles', ['$resource',
        function ($resource) {
            return $resource('http://127.0.0.1:8888/api/algorithms/available-algorithm-files', {}, {
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
