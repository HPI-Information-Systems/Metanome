'use strict';

angular.module('Metanome')
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
                },
                execution: {
                    method: 'DELETE',
                    params: {
                        input: 'executions',
                        id: '@id'
                    }
                }
            });
        }
    ])

;
