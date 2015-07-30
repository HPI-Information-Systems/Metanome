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

  $scope.functionalDependency = {
    count: 0,
    data: [],
    query: {
      order: '',
      limit: 5,
      page: 1
    },
    selected: [],
    params: {
      type: 'Functional Dependency',
      sort: 'Determinant',
      from: '0',
      to: '10'
    }
  }

  $scope.onpagechange = onpagechange

  loadColumnCombination()
  loadFunctionalDependency()

  function loadColumnCombination() {
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

  function loadFunctionalDependency() {
    Results.get($scope.functionalDependency.params, function(res) {
      var rows = []
      res.forEach(function(result) {
        var combinations = []
        result.result.determinant.columnIdentifiers.forEach(function(combination) {
          combinations.push(combination.tableIdentifier+'.'+combination.columnIdentifier)
        })
        rows.push({
          determinant: '[' + combinations.join(',') + ']',
          dependant: result.dependant.tableIdentifier + '.' + result.dependant.columnIdentifier,
          extendedDependant: result.extendedDependant,
          determinantColumnRatio: result.determinantColumnRatio,
          dependantColumnRatio: result.dependantColumnRatio,
          determinantOccurrenceRatio: result.determinantOccurrenceRatio,
          dependantOccurrenceRatio: result.dependantOccurrenceRatio,
          generalCoverage: result.generalCoverage,
          determinantUniquenessRatio: result.determinantUniquenessRatio,
          dependantUniquenessRatio: result.dependantUniquenessRatio,
          pollution: result.pollution,
          pollutionColumn: result.pollutionColumn,
          informationGainCell: result.informationGainCell,
          informationGainByte: result.informationGainByte
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
      $scope.functionalDependency.data = rows
    })
  }

  function onpagechange(page, limit) {
    var deferred = $q.defer();

    $timeout(function () {
      deferred.resolve();
    }, 2000);

    return deferred.promise;
  }

})
