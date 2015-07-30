'use strict';

var app = angular.module('v2')

app.controller('ResultCtrl', function ($scope, $log, Executions, $rootScope, $http, $q, $timeout) {

  $scope.selected = [];

  $scope.query = {
    order: 'name',
    limit: 5,
    page: 1
  };

  $scope.uniqueColumnCombination = {
    count: 0,
    data: []
  }

  $scope.onpagechange = function(page, limit) {
    var deferred = $q.defer();

    $timeout(function () {
      deferred.resolve();
    }, 2000);

    return deferred.promise;
  };

  $scope.onorderchange = function(order) {
    var deferred = $q.defer();

    $timeout(function () {
      deferred.resolve();
    }, 2000);

    return deferred.promise;
  };


      $http.get('http://127.0.0.1:8888/api/result-store/get-from-to/Unique Column Combination/x/true/0/15').then(function(res) {
      switch('Unique Column Combination') {
        case 'Unique Column Combination':
          var rows = []
          res.data.forEach(function(result) {
            var combinations = []
            result.result.columnCombination.columnIdentifiers.forEach(function(combination) {
              combinations.push(combination.tableIdentifier+'.'+combination.columnIdentifier)
            })
            rows.push({
              columnCombination: '[' + combinations.join(',') + ']',
              columnRatio: result.columnRatio,
              occurrenceRatio: result.occurrenceRatio,
              uniquenessRatio: result.uniquenessRatio,
              randomness: result.randomness
            })
          })
          var headers = [{
              name: 'Column Combination',
              key: 'columnCombination'
            },
            {
              name: 'Column Ratio',
              key: 'columnRatio'
            },
            {
              name: 'Occurrence Ratio',
              key: 'occurrenceRatio'
            },
            {
              name: 'Uniqueness Ratio*',
              key: 'uniquenessRatio'
            },
            {
              name: 'Randomness*',
              key: 'randomness'
            }
          ]
          $scope.uniqueColumnCombination.data = rows
          break
      }
   });
})
