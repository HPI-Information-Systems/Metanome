'use strict';

var app = angular.module('v2')

.config(function config( $stateProvider ) {
  $stateProvider
    .state('result', {
      url: '/result/:resultId?cached&file&extended',
      views: {
        'main@': {
            controller: 'ResultCtrl',
            templateUrl: 'app/result/result.html'
         }
      }
    })
})

app.controller('ResultCtrl', function ($scope, $log, Executions, Results, $q, usSpinnerService,
                                       $timeout, $stateParams, LoadResults, Execution, File, ngDialog, $http) {

  $scope.id = $stateParams.resultId
  $scope.extended = $stateParams.extended || false

  $scope.openFDVisualization = openFDVisualization
  $scope.openUCCVisualization = openUCCVisualization

  $scope.uniqueColumnCombination = {
    count: 0,
    data: [],
    query: {
      order: '',
      limit: 10,
      page: 1
    },
    selected: [],
    params: {
      type: 'Unique Column Combination',
      sort: 'Column Combination',
      from: 0,
      to: 50
    }
  }

  $scope.functionalDependency = {
    count: 0,
    data: [],
    query: {
      order: '',
      limit: 10,
      page: 1
    },
    selected: [],
    params: {
      type: 'Functional Dependency',
      sort: 'Determinant',
      from: 0,
      to: 50
    }
  }

  $scope.basicStatistic = {
    count: 0,
    data: [],
    query: {
      order: '',
      limit: 10,
      page: 1
    },
    selected: [],
    params: {
      type: 'Basic Statistic',
      sort: 'Statistic Name',
      from: 0,
      to: 50
    }
  }

  $scope.inclusionDependency = {
    count: 0,
    data: [],
    query: {
      order: '',
      limit: 10,
      page: 1
    },
    selected: [],
    params: {
      type: 'Inclusion Dependency',
      sort: 'Dependant',
      from: '0',
      to: '15'
    }
  }

  $scope.conditionalUniqueColumnCombination = {
    count: 0,
    data: [],
    query: {
      order: '',
      limit: 10,
      page: 1
    },
    selected: [],
    params: {
      type: 'Conditional Unique Column Combination',
      sort: 'Dependant',
      from: '0',
      to: '15'
    }
  }

  $scope.onPageChangeBS = onPageChangeBS
  $scope.onPageChangeUCC = onPageChangeUCC
  $scope.onPageChangeFD = onPageChangeFD
  $scope.onPageChangeID = onPageChangeID
  $scope.onPageChangeCUCC = onPageChangeCUCC

  if (!$stateParams.cached && !$stateParams.file) {
    startSpin()
    LoadResults.load({id: $scope.id, detailed: ($stateParams.extended || false)}, function () {
      stopSpin()
      init()
      loadDetailsForExecution()
    })
  } else if ($stateParams.file) {
      startSpin()
      console.log("File")
      LoadResults.file({id: $scope.id, detailed: ($stateParams.extended || false)}, function () {
        loadDetailsForFile()
        init()
        stopSpin()
      })
  } else {
    init()
    loadDetailsForExecution()
  }

  function init() {
    $http.get('http://127.0.0.1:8888/api/result-store/count/' + $scope.uniqueColumnCombination.params.type).
        then(function(response) {
          var count = response.data
          if(count > 0){
            $scope.uniqueColumnCombination.count = count
            loadColumnCombination()
          }
    })
    $http.get('http://127.0.0.1:8888/api/result-store/count/' + $scope.functionalDependency.params.type).
        then(function(response) {
          var count = response.data
          if(count > 0){
            $scope.functionalDependency.count = count
            loadFunctionalDependency()
          }
    })
    $http.get('http://127.0.0.1:8888/api/result-store/count/' + $scope.basicStatistic.params.type).
      then(function(response) {
        var count = response.data
        if(count > 0){
          $scope.basicStatistic.count = count
          loadBasicStatistic()
        }
    })
    $http.get('http://127.0.0.1:8888/api/result-store/count/' + $scope.inclusionDependency.params.type).
        then(function(response) {
          var count = response.data
          if(count > 0){
            $scope.inclusionDependency.count = count
            loadInclusionDependency()
          }
        })
    $http.get('http://127.0.0.1:8888/api/result-store/count/' + $scope.conditionalUniqueColumnCombination.params.type).
        then(function(response) {
          var count = response.data
          if(count > 0){
            $scope.conditionalUniqueColumnCombination.count = count
            loadConditionalUniqueColumnCombination()
          }
        })
  }
  function loadDetailsForFile() {
    File.get({id: $scope.id}, function(result) {
      $scope.file = result
    })
  }
  function loadDetailsForExecution() {
    Execution.get({id: $scope.id}, function(result) {
      $scope.execution = result
      var duration = result.end - result.begin
      $scope.duration = Math.floor(duration/(60*60*24))+'d '+twoDigets(Math.floor(duration/(60*60)))+':'+twoDigets(Math.floor((duration/60)%60)) + ':' + twoDigets(Math.floor(duration%60))
    })
  }

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
      $scope.uniqueColumnCombination.data = $scope.uniqueColumnCombination.data.concat(rows)
    })
  }

  function loadConditionalUniqueColumnCombination() {
    Results.get($scope.conditionalUniqueColumnCombination.params, function(res) {
      var rows = []
      res.forEach(function(result) {
        var combinations = []
        result.result.columnCombination.columnIdentifiers.forEach(function(combination) {
          combinations.push(combination.tableIdentifier+'.'+combination.columnIdentifier)
        })
        rows.push({
          columnCombination: '[' + combinations.join(',') + ']',
          condition: result.result.condition.columnIdentifier.tableIdentifier + '.' + result.result.condition.columnIdentifier.columnIdentifier + (result.result.condition.negated ? '!=':'=') + result.result.condition.columnValue,
          coverage: result.result.condition.coverage,
          columnRatio: result.columnRatio,
          occurrenceRatio: result.occurrenceRatio,
          uniquenessRatio: result.uniquenessRatio
        })
      })
      $scope.conditionalUniqueColumnCombination.data = $scope.conditionalUniqueColumnCombination.data.concat(rows)
    })
  }

  function loadFunctionalDependency() {
    Results.get($scope.functionalDependency.params, function(res) {
      var rows = []
      res.forEach(function(result) {
        var determinant = []
        result.result.determinant.columnIdentifiers.forEach(function(combination) {
          determinant.push(combination.tableIdentifier+'.'+combination.columnIdentifier)
        })
        var extendedDependant = []
        if(result.extendedDependant) {
          result.extendedDependant.columnIdentifiers.forEach(function (combination) {
            extendedDependant.push(combination.tableIdentifier + '.' + combination.columnIdentifier)
          })
        }
        rows.push({
          determinant: '[' + determinant.join(',') + ']',
          dependant: result.dependant.tableIdentifier + '.' + result.dependant.columnIdentifier,
          extendedDependant: '[' + extendedDependant.join(',') + ']',
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
      $scope.functionalDependency.data = $scope.functionalDependency.data.concat(rows)
    })
  }

  function loadBasicStatistic() {
    Results.get($scope.basicStatistic.params, function(res) {
      var rows = []
      res.forEach(function(result) {
        var combinations = []
        result.result.columnCombination.columnIdentifiers.forEach(function(combination) {
          combinations.push(combination.tableIdentifier+'.'+combination.columnIdentifier)
        })
        rows.push({
          statisticName: result.statisticName,
          columnCombination: '[' + combinations.join(',') + ']',
          value: result.value,
          columnRatio: result.columnRatio,
          occurenceRatio: result.occurenceRatio,
          uniquenessRatio: result.uniquenessRatio
        })
      })
      $scope.basicStatistic.data = $scope.basicStatistic.data.concat(rows)
    })
  }


  function loadInclusionDependency() {
    Results.get($scope.inclusionDependency.params, function(res) {
      var rows = []
      res.forEach(function(result) {
        var combinations = []
        result.result.dependant.columnIdentifiers.forEach(function(combination) {
          combinations.push(combination.tableIdentifier+'.'+combination.columnIdentifier)
        })
        var referenced = []
        result.result.referenced.columnIdentifiers.forEach(function(combination) {
          referenced.push(combination.tableIdentifier+'.'+combination.columnIdentifier)
        })
        rows.push({
          dependant: '[' + combinations.join(',') + ']',
          referenced: '[' + referenced.join(',') + ']',
          dependantColumnRatio: result.dependantColumnRatio,
          referencedColumnRatio: result.referencedColumnRatio,
          dependantOccurrenceRatio: result.dependantOccurrenceRatio,
          referencedOccurrenceRatio: result.referencedOccurrenceRatio,
          generalCoverage: result.generalCoverage,
          dependantUniquenessRatio: result.dependantUniquenessRatio,
          referencedUniquenessRatio: result.referencedUniquenessRatio
        })
      })
      $scope.inclusionDependency.data = $scope.inclusionDependency.data.concat(rows)
    })
  }

  function openFDVisualization() {
    $scope.openVisualizationType = 'FD'
    ngDialog.open({
      template: '/assets/visualization.html',
      scope: $scope
    })
  }
  function openUCCVisualization() {
    $scope.openVisualizationType = 'UCC'
    ngDialog.open({
      template: '/assets/visualization.html',
      scope: $scope
    })
  }

  function onPageChangeFD(page, limit) {
    var deferred = $q.defer();
    if($scope.functionalDependency.params.to < $scope.functionalDependency.count) {
      $scope.functionalDependency.params.from += $scope.functionalDependency.params.to + 1
      $scope.functionalDependency.params.to += Math.max(limit, $scope.functionalDependency.count)
      loadFunctionalDependency()
      $timeout(function () {
        deferred.resolve();
      }, 500);
    } else {
      deferred.resolve()
    }
    return deferred.promise;
  }
  function onPageChangeUCC(page, limit) {
    var deferred = $q.defer();
    if($scope.uniqueColumnCombination.params.to < $scope.uniqueColumnCombination.count) {
      $scope.uniqueColumnCombination.params.from += $scope.uniqueColumnCombination.params.to + 1
      $scope.uniqueColumnCombination.params.to += Math.max(limit, $scope.uniqueColumnCombination.count)
      loadColumnCombination()
      $timeout(function () {
        deferred.resolve();
      }, 500);
    } else {
      deferred.resolve()
    }
    return deferred.promise;
  }
  function onPageChangeBS(page, limit) {
    var deferred = $q.defer();
    if($scope.basicStatistic.params.to < $scope.basicStatistic.count) {
      $scope.basicStatistic.params.from += $scope.basicStatistic.params.to + 1
      $scope.basicStatistic.params.to += Math.max(limit, $scope.basicStatistic.count)
      loadBasicStatistic()
      $timeout(function () {
        deferred.resolve();
      }, 500);
    } else {
      deferred.resolve()
    }
    return deferred.promise;
  }
  function onPageChangeID(page, limit) {
    var deferred = $q.defer();
    if($scope.inclusionDependency.params.to < $scope.inclusionDependency.count) {
      $scope.inclusionDependency.params.from += $scope.inclusionDependency.params.to + 1
      $scope.inclusionDependency.params.to += Math.max(limit, $scope.inclusionDependency.count)
      loadInclusionDependency()
      $timeout(function () {
        deferred.resolve();
      }, 500);
    } else {
      deferred.resolve()
    }
    return deferred.promise;
  }
  function onPageChangeCUCC(page, limit) {
    var deferred = $q.defer();
    if($scope.conditionalUniqueColumnCombination.params.to < $scope.conditionalUniqueColumnCombination.count) {
      $scope.conditionalUniqueColumnCombination.params.from += $scope.conditionalUniqueColumnCombination.params.to + 1
      $scope.conditionalUniqueColumnCombination.params.to += Math.max(limit, $scope.conditionalUniqueColumnCombination.count)
      loadConditionalUniqueColumnCombinationy()
      $timeout(function () {
        deferred.resolve();
      }, 500);
    } else {
      deferred.resolve()
    }
    return deferred.promise;
  }
  function startSpin(){
    usSpinnerService.spin('spinner-2');
  }
  function stopSpin(){
    usSpinnerService.stop('spinner-2');
  }
  function twoDigets(number) {
    return (number < 10 ? '0'+number : ''+number)
  }

})
