'use strict';

angular.module('v2')
    .factory('New', ['$resource',
        function ($resource) {
            return $resource('http://127.0.0.1:8888/api/:type/store', {}, {
                algorithm: {
                    method: 'POST',
                    params: {
                        type: 'algorithms'
                    }
                },
                fileInput: {
                    method: 'POST',
                    params: {
                        type: 'file-inputs'
                    }
                },
                tableInput: {
                    method: 'POST',
                    params: {
                        type: 'table-inputs'
                    }
                },
                databaseConnection: {
                    method: 'POST',
                    params: {
                        type: 'database-connections'
                    }
                }
            });
        }
    ])

; 
