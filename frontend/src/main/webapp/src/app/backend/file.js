'use strict';

angular.module('Metanome')
    .factory('File', ['$resource',
        function ($resource) {
            return $resource('http://127.0.0.1:8888/api/file-inputs/get/:id', {}, {
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
