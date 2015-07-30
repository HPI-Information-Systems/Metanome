'use strict';

var app = angular.module('v2')

.config(function config( $stateProvider ) {
  $stateProvider
    .state('result', {
      url: '/result/:resultId',
      views: {
        'main@': {
            controller: 'ResultCtrl',
            templateUrl: 'app/result/result.html'
         }
      }
    })
})

app.controller('ResultCtrl', function ($scope, $log, Executions, Results, $q, $timeout) {

  $scope.uniqueColumnCombination = {
    count: 0,
    data: [],
    query: {
        order: '',
        limit: 5,
        page: 1
    },
    selected: [],
    params: {
      type: 'Unique Column Combination',
      sort: 'Column Combination',
      from: '0',
      to: '10'
    }
  }

  loadResult()

  function loadResult() {
    console.log("Load Result")
    Results.get($scope.uniqueColumnCombination.params, function(res) {
       var rows = []
       res.forEach(function(result) {
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
     })
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

})
