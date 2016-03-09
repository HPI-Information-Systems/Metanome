'use strict';

var app = angular.module('Metanome')

  .config(function config($stateProvider) {
    $stateProvider
      .state('history', {
        url: '/history',
        views: {
          'main@': {
            controller: 'HistoryCtrl',
            templateUrl: 'app/history/history.html'
          }
        }
      })
  });

app.controller('HistoryCtrl', function ($scope, $log, Executions, $filter, $location, ngDialog, $timeout, Delete, usSpinnerService) {

  // ** VARIABLE DEFINITIONS **
  // **************************

  $scope.content = [];

  $scope.historyTable = {
    count: 0,
    query: {
      order: '',
      limit: 10,
      page: 1
    },
    params: {
      sort: 'date',
      from: 0,
      to: 10
    }
  };

  // Private variables
  var executions;


  // ** FUNCTION DEFINITIONS **
  // **************************

  function twoDigets(number) {
    return (number < 10 ? '0' + number : '' + number)
  }

  function loadExecutions() {
    Executions.getAll({}, function (result) {
      executions = result;
      $scope.content = [];
      result.forEach(function (execution) {
        var duration = execution.end - execution.begin;
        if (execution.end === 0) {
          duration = 0
        }
        var inputs = [];
        var results = [];
        execution.inputs.forEach(function (input) {
          input.name = input.name.replace(/^.*[\\\/]/, '');
          inputs.push(input.name)
        });
        execution.results.forEach(function (result) {
          results.push(result.typeName)
        });
        if (execution.aborted) {
          results = ['EXECUTION ABORTED']
        }

        var days = Math.floor(duration / (1000 * 60 * 60 * 24));
        var hours = twoDigets(Math.floor(duration / (1000 * 60 * 60)));
        var minutes = twoDigets(Math.floor((duration / (1000 * 60)) % 60));
        var seconds = twoDigets(Math.floor((duration / 1000) % 60));
        var milliseconds = Math.floor(duration % 1000);

        var executionTimeStr;
        if (seconds === '00') {
          executionTimeStr = milliseconds + ' ms';
        } else if (days === 0) {
          executionTimeStr = hours + ':' + minutes + ':' + seconds + ' (hh:mm:ss) and ' + milliseconds + ' ms';
        } else {
          executionTimeStr = days + ' day(s) and ' + hours + ':' + minutes + ':' + seconds + ' (hh:mm:ss) and ' + milliseconds + ' ms';
        }

        $scope.content.push({
          id: execution.id,
          name: execution.algorithm.name,
          date: $filter('date')(execution.begin, 'yyyy-MM-dd HH:mm:ss'),
          time: executionTimeStr,
          inputs: inputs.join(',\n'),
          resultType: results.join(', '),
          actions: '',
          aborted: execution.aborted,
          count: execution.countResult,
          cached: !execution.countResult,
          fd: execution.algorithm.fd,
          ind: execution.algorithm.ind,
          ucc: execution.algorithm.ucc,
          cucc: execution.algorithm.cucc,
          od: execution.algorithm.od,
          basicStat: execution.algorithm.basicStat
        })
      });
      var orderBy = $filter('orderBy');
      $scope.content = orderBy($scope.content, $scope.historyTable.params.sort, true);
      $scope.historyTable.count = $scope.content.length;
    })
  }


  function showResult(result) {
        if (!result.aborted) {
          $location.url('/result/' + result.id + '?count=' + result.count + '&cached=' + result.cached +
          '&load=true' + '&ind=' + result.ind + '&fd=' + result.fd + '&ucc=' + result.ucc +
          '&cucc=' + result.cucc + '&od=' + result.od + '&basicStat=' + result.basicStat);
        }
      }

  function startSpin() {
    $timeout(function() {
      usSpinnerService.spin('spinner-2');
    }, 100);
  }

  function stopSpin() {
    usSpinnerService.stop('spinner-2');
  }

  function confirmDelete(execution) {
        $scope.confirmText = 'Are you sure you want to delete it?';
        $scope.confirmItem = execution;
        $scope.confirmFunction = function () {
          $scope.startSpin();
          Delete.execution({id: $scope.confirmItem.id}, function () {
            $scope.loadExecutions()
          });
          $scope.stopSpin();
          ngDialog.closeAll();
        };
        ngDialog.openConfirm({
          /*jshint multistr: true */
          template: '\
                <h3>Confirm</h3>\
                <p>{{$parent.confirmText}}</p>\
                <div class="ngdialog-buttons">\
                    <button type="button" class="ngdialog-button ngdialog-button-secondary" ng-click="closeThisDialog(0)">No</button>\
                    <button type="button" class="ngdialog-button ngdialog-button-warning" ng-click="$parent.confirmFunction(1)">Yes</button>\
                </div>',
          plain: true,
          scope: $scope
        })
      }

  // ** EXPORT FUNCTIONS **
  // **********************

  $scope.loadExecutions = loadExecutions;
  $scope.confirmDelete = confirmDelete;
  $scope.showResult = showResult;
  $scope.startSpin = startSpin;
  $scope.stopSpin = stopSpin;


  // ** FUNCTION CALLS **
  // ********************

  loadExecutions();
});
