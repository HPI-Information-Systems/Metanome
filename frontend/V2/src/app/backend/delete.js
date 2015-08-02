'use strict';

angular.module('v2')
    .factory('Delete', ['$resource',
        function ($resource) {
            return $resource('http://127.0.0.1:8888/api/:input/delete/:id', {}, {
                file: {
                    method: 'DELETE',
                    params: {
                        input: 'file-inputs',
                        id: '@id'
                    }
                },
                table: {
                    method: 'DELETE',
                    params: {
                        input: 'table-inputs',
                        id: '@id'
                    }
                },
                database: {
                    method: 'DELETE',
                    params: {
                        input: 'database-connections',
                        id: '@id'
                    }
                },
                algorithm: {
                    method: 'DELETE',
                    params: {
                        input: 'algorithms',
                        id: '@id'
                    }
                }
            });
        }
    ])

;
