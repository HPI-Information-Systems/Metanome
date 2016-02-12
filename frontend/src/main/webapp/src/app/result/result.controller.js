'use strict';

var app = angular.module('Metanome')

  .config(function config($stateProvider) {
    $stateProvider
      .state('result', {
        url: '/result/:resultId?cached&count&load&file&extended&ind&od&ucc&cucc&fd&basicStat',
        views: {
          'main@': {
            controller: 'ResultCtrl',
            templateUrl: 'app/result/result.html'
          }
        }
      })
  });

app.controller('ResultCtrl', function ($scope, $log, Executions, Results, $q, usSpinnerService,
                                       $timeout, $stateParams, LoadResults, CountResults, Execution, File,
                                       ngDialog, $http) {

  // ** VARIABLE DEFINITIONS **
  // **************************

  $scope.id = $stateParams.resultId;
  $scope.extended = ($stateParams.extended === 'true');
  $scope.cached = ($stateParams.cached === 'true');
  $scope.file = ($stateParams.file === 'true');
  $scope.count = ($stateParams.count === 'true');
  $scope.fd = ($stateParams.fd === 'true');
  $scope.ind = ($stateParams.ind === 'true');
  $scope.ucc = ($stateParams.ucc === 'true');
  $scope.cucc = ($stateParams.cucc === 'true');
  $scope.od = ($stateParams.od === 'true');
  $scope.basicStat = ($stateParams.basicStat === 'true');
  $scope.load = ($stateParams.load === 'true');

  $scope.basicStatisticColumnNames = [];

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
  };

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
  };

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
      sort: 'Column Combination',
      from: 0,
      to: 50
    }
  };

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
  };

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
  };

  $scope.orderDependency = {
    count: 0,
    data: [],
    query: {
      order: '',
      limit: 10,
      page: 1
    },
    selected: [],
    params: {
      type: 'Order Dependency',
      sort: 'LHS',
      from: '0',
      to: '15'
    }
  };

  // ** FUNCTION DEFINITIONS **
  // **************************

  function removeDuplicates(arr){
    var retArray = [];
    for (var a = arr.length - 1; a >= 0; a--) {
      for (var b = arr.length - 1; b >= 0; b--) {
        if(arr[a] === arr[b] && a !== b){
          delete arr[b];
        }
      }
      if(arr[a] !== undefined) {
        retArray.push(arr[a]);
      }
    }
    return retArray;
  }

  function loadColumnCombination() {
    Results.get($scope.uniqueColumnCombination.params, function (res) {
      var rows = [];
      res.forEach(function (result) {
        var combinations = [];
        result.result.columnCombination.columnIdentifiers.forEach(function (combination) {
          combinations.push(combination.tableIdentifier + '.' + combination.columnIdentifier)
        });
        rows.push({
          columnCombination: '[' + combinations.join(', ') + ']',
          columnRatio: result.columnRatio,
          occurrenceRatio: result.occurrenceRatio,
          uniquenessRatio: result.uniquenessRatio,
          randomness: result.randomness
        })
      });
      $scope.uniqueColumnCombination.data = $scope.uniqueColumnCombination.data.concat(rows)
    })
  }

  function loadConditionalUniqueColumnCombination() {
    Results.get($scope.conditionalUniqueColumnCombination.params, function (res) {
      var rows = [];
      res.forEach(function (result) {
        var combinations = [];
        result.result.columnCombination.columnIdentifiers.forEach(function (combination) {
          combinations.push(combination.tableIdentifier + '.' + combination.columnIdentifier)
        });
        rows.push({
          columnCombination: '[' + combinations.join(', ') + ']',
          condition: result.result.condition.columnIdentifier.tableIdentifier + '.' + result.result.condition.columnIdentifier.columnIdentifier + (result.result.condition.negated ? ' != ' : ' = ') + result.result.condition.columnValue,
          coverage: result.result.condition.coverage,
          columnRatio: result.columnRatio,
          occurrenceRatio: result.occurrenceRatio,
          uniquenessRatio: result.uniquenessRatio
        })
      });
      $scope.conditionalUniqueColumnCombination.data = $scope.conditionalUniqueColumnCombination.data.concat(rows)
    })
  }

  function loadFunctionalDependency() {
    Results.get($scope.functionalDependency.params, function (res) {
      var rows = [];
      res.forEach(function (result) {
        var determinant = [];
        result.result.determinant.columnIdentifiers.forEach(function (combination) {
          if (combination.tableIdentifier && combination.columnIdentifier) {
            determinant.push(combination.tableIdentifier + '.' + combination.columnIdentifier);
          } else {
            determinant.push('');
          }
        });
        var extendedDependant = [];
        if (result.extendedDependant) {
          result.extendedDependant.columnIdentifiers.forEach(function (combination) {
            if (combination.tableIdentifier && combination.columnIdentifier) {
              extendedDependant.push(combination.tableIdentifier + '.' + combination.columnIdentifier)
            } else {
              determinant.push('');
            }
          })
        }
        var dependant = '';
        if (result.dependant.tableIdentifier && result.dependant.columnIdentifier) {
          dependant = result.dependant.tableIdentifier + '.' + result.dependant.columnIdentifier;
        }

        rows.push({
          determinant: '[' + determinant.join(', ') + ']',
          dependant: dependant,
          extendedDependant: '[' + extendedDependant.join(', ') + ']',
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
      });
      $scope.functionalDependency.data = $scope.functionalDependency.data.concat(rows)
    })
  }

  function loadBasicStatistic() {
    Results.get($scope.basicStatistic.params, function (res) {

      // getting all column names
      var columnNames = [];
      res.forEach(function (result) {
        for (var columnName in result.statisticMap) {
          columnNames.push(columnName)
        }
      });
      // remove duplicates in column names
      $scope.basicStatisticColumnNames = removeDuplicates(columnNames);

      var rows = [];
      res.forEach(function (result) {
        var combinations = [];
        result.result.columnCombination.columnIdentifiers.forEach(function (combination) {
          combinations.push(combination.tableIdentifier + '.' + combination.columnIdentifier)
        });
        var entry = {
          columnCombination: '[' + combinations.join(', ') + ']',
          columnRatio: result.columnRatio,
          occurenceRatio: result.occurenceRatio,
          uniquenessRatio: result.uniquenessRatio,
          values: []
        };
        $scope.basicStatisticColumnNames.forEach(function(column) {
          if (column in result.result.statisticMap) {
            entry.values.push(result.result.statisticMap[column].value);
          } else {
            entry.values.push('-');
          }
        });
        rows.push(entry)
      });
      $scope.basicStatistic.data = $scope.basicStatistic.data.concat(rows)
    })
  }


  function loadInclusionDependency() {
    Results.get($scope.inclusionDependency.params, function (res) {
      var rows = [];
      res.forEach(function (result) {
        var combinations = [];
        result.result.dependant.columnIdentifiers.forEach(function (combination) {
          combinations.push(combination.tableIdentifier + '.' + combination.columnIdentifier)
        });
        var referenced = [];
        result.result.referenced.columnIdentifiers.forEach(function (combination) {
          referenced.push(combination.tableIdentifier + '.' + combination.columnIdentifier)
        });
        rows.push({
          dependant: '[' + combinations.join(', ') + ']',
          referenced: '[' + referenced.join(', ') + ']',
          dependantColumnRatio: result.dependantColumnRatio,
          referencedColumnRatio: result.referencedColumnRatio,
          dependantOccurrenceRatio: result.dependantOccurrenceRatio,
          referencedOccurrenceRatio: result.referencedOccurrenceRatio,
          generalCoverage: result.generalCoverage,
          dependantUniquenessRatio: result.dependantUniquenessRatio,
          referencedUniquenessRatio: result.referencedUniquenessRatio
        })
      });
      $scope.inclusionDependency.data = $scope.inclusionDependency.data.concat(rows)
    })
  }

  function loadOrderDependency() {
    Results.get($scope.orderDependency.params, function (res) {
      var rows = [];
      res.forEach(function (result) {
        var combinations = [];
        result.result.lhs.columnIdentifiers.forEach(function (combination) {
          combinations.push(combination.tableIdentifier + '.' + combination.columnIdentifier)
        });
        var referenced = [];
        result.result.rhs.columnIdentifiers.forEach(function (combination) {
          referenced.push(combination.tableIdentifier + '.' + combination.columnIdentifier)
        });
        rows.push({
          LHS: '[' + combinations.join(', ') + ']',
          RHS: '[' + referenced.join(', ') + ']',
          LHSColumnRatio: result.lhsColumnRatio,
          RHSColumnRatio: result.rhsColumnRatio,
          GeneralCoverage: result.generalCoverage,
          LHSOccurrenceRatio: result.lhsOccurrenceRatio,
          RHSOccurrenceRatio: result.rhsOccurrenceRatio,
          LHSUniquenessRatio: result.lhsUniquenessRatio,
          RHSUniquenessRatio: result.rhsUniquenessRatio
        })
      });
      $scope.orderDependency.data = $scope.orderDependency.data.concat(rows)
    })
  }

  function init() {
    if ($scope.ucc || $scope.file) {
      $http.get('http://127.0.0.1:8888/api/result-store/count/' + $scope.uniqueColumnCombination.params.type).
        then(function (response) {
          var count = response.data;
          if (count > 0) {
            $scope.uniqueColumnCombination.count = count;
            if (!$scope.count) {
              loadColumnCombination()
            }
          }
        });
    }
    if ($scope.fd || $scope.file) {
      $http.get('http://127.0.0.1:8888/api/result-store/count/' + $scope.functionalDependency.params.type).
        then(function (response) {
          var count = response.data;
          if (count > 0) {
            $scope.functionalDependency.count = count;
            if (!$scope.count) {
              loadFunctionalDependency()
            }
          }
        });
    }
    if ($scope.basicStat || $scope.file) {
      $http.get('http://127.0.0.1:8888/api/result-store/count/' + $scope.basicStatistic.params.type).
        then(function (response) {
          var count = response.data;
          if (count > 0) {
            $scope.basicStatistic.count = count;
            if (!$scope.count) {
              loadBasicStatistic()
            }
          }
        });
    }
    if ($scope.ind || $scope.file) {
      $http.get('http://127.0.0.1:8888/api/result-store/count/' + $scope.inclusionDependency.params.type).
        then(function (response) {
          var count = response.data;
          if (count > 0) {
            $scope.inclusionDependency.count = count;
            if (!$scope.count) {
              loadInclusionDependency()
            }
          }
        });
    }
    if ($scope.cucc || $scope.file) {
      $http.get('http://127.0.0.1:8888/api/result-store/count/' + $scope.conditionalUniqueColumnCombination.params.type).
        then(function (response) {
          var count = response.data;
          if (count > 0) {
            $scope.conditionalUniqueColumnCombination.count = count;
            if (!$scope.count) {
              loadConditionalUniqueColumnCombination()
            }
          }
        });
    }
    if ($scope.od || $scope.file) {
      $http.get('http://127.0.0.1:8888/api/result-store/count/' + $scope.orderDependency.params.type).
        then(function (response) {
          var count = response.data;
          if (count > 0) {
            $scope.orderDependency.count = count;
            if (!$scope.count) {
              loadOrderDependency()
            }
          }
        })
    }
  }

  function loadDetailsForFile() {
    File.get({id: $scope.id}, function (result) {
      $scope.file = result;
      $scope.file.shortFileName = $scope.file.fileName.replace(/^.*[\\\/]/, '');
    })
  }

  function twoDigets(number) {
    return (number < 10 ? '0' + number : '' + number)
  }

  function loadDetailsForExecution() {
    Execution.get({id: $scope.id}, function (result) {
      $scope.execution = result;
      var duration = result.end - result.begin;
      $scope.duration = Math.floor(duration / (60 * 60 * 24)) + 'd ' + twoDigets(Math.floor(duration / (60 * 60))) + ':' + twoDigets(Math.floor((duration / 60) % 60)) + ':' + twoDigets(Math.floor(duration % 60))
    })
  }


  function openFDVisualization() {
    $scope.openVisualizationType = 'FD';
    ngDialog.open({
      template: '/assets/visualization.html',
      scope: $scope
    })
  }

  function openUCCVisualization() {
    $scope.openVisualizationType = 'UCC';
    ngDialog.open({
      template: '/assets/visualization.html',
      scope: $scope
    })
  }

  function onPageChangeFD(page, limit) {
    var deferred = $q.defer();
    if ($scope.functionalDependency.params.to < $scope.functionalDependency.count) {
      $scope.functionalDependency.params.from += $scope.functionalDependency.params.to + 1;
      $scope.functionalDependency.params.to += Math.max(limit, $scope.functionalDependency.count);
      loadFunctionalDependency();
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
    if ($scope.uniqueColumnCombination.params.to < $scope.uniqueColumnCombination.count) {
      $scope.uniqueColumnCombination.params.from += $scope.uniqueColumnCombination.params.to + 1;
      $scope.uniqueColumnCombination.params.to += Math.max(limit, $scope.uniqueColumnCombination.count);
      loadColumnCombination();
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
    if ($scope.basicStatistic.params.to < $scope.basicStatistic.count) {
      $scope.basicStatistic.params.from += $scope.basicStatistic.params.to + 1;
      $scope.basicStatistic.params.to += Math.max(limit, $scope.basicStatistic.count);
      loadBasicStatistic();
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
    if ($scope.inclusionDependency.params.to < $scope.inclusionDependency.count) {
      $scope.inclusionDependency.params.from += $scope.inclusionDependency.params.to + 1;
      $scope.inclusionDependency.params.to += Math.max(limit, $scope.inclusionDependency.count);
      loadInclusionDependency();
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
    if ($scope.conditionalUniqueColumnCombination.params.to < $scope.conditionalUniqueColumnCombination.count) {
      $scope.conditionalUniqueColumnCombination.params.from += $scope.conditionalUniqueColumnCombination.params.to + 1;
      $scope.conditionalUniqueColumnCombination.params.to += Math.max(limit, $scope.conditionalUniqueColumnCombination.count);
      loadConditionalUniqueColumnCombination();
      $timeout(function () {
        deferred.resolve();
      }, 500);
    } else {
      deferred.resolve()
    }
    return deferred.promise;
  }

  function onPageChangeOD(page, limit) {
    var deferred = $q.defer();
    if ($scope.orderDependency.params.to < $scope.orderDependency.count) {
      $scope.orderDependency.params.from += $scope.orderDependency.params.to + 1;
      $scope.orderDependency.params.to += Math.max(limit, $scope.orderDependency.count);
      loadOrderDependency();
      $timeout(function () {
        deferred.resolve();
      }, 500);
    } else {
      deferred.resolve()
    }
    return deferred.promise;
  }

  function startSpin() {
    usSpinnerService.spin('spinner-2');
  }

  function stopSpin() {
    usSpinnerService.stop('spinner-2');
  }

  // ** EXPORT FUNCTIONS **
  // **********************

  $scope.openFDVisualization = openFDVisualization;
  $scope.openUCCVisualization = openUCCVisualization;
  $scope.onPageChangeBS = onPageChangeBS;
  $scope.onPageChangeUCC = onPageChangeUCC;
  $scope.onPageChangeFD = onPageChangeFD;
  $scope.onPageChangeID = onPageChangeID;
  $scope.onPageChangeCUCC = onPageChangeCUCC;
  $scope.onPageChangeOD = onPageChangeOD;


  // ** FUNCTION CALLS **
  // ********************

  // load extended results
  if ($scope.extended) {
    startSpin();
    LoadResults.load({id: $scope.id, notDetailed: false}, function () {
      stopSpin();
      init();
      loadDetailsForExecution()
    });
    // load all results for a file
  } else if ($scope.file) {
    loadDetailsForFile();
    startSpin();
    LoadResults.file({id: $scope.id, notDetailed: true}, function () {
      init();
      stopSpin()
    });
    // load result (coming from history)
  } else if ($scope.load) {
    startSpin();
    LoadResults.load({id: $scope.id, notDetailed: true}, function () {
      stopSpin();
      init();
      loadDetailsForExecution()
    });
    // load results
  } else {
    init();
    loadDetailsForExecution();
    stopSpin();
  }

});
