'use strict';

var app = angular.module('Metanome')

.config(function config( $stateProvider ) {
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

app.controller('HistoryCtrl', function ($scope, $log, Executions, $filter) {

  // Public variables
  $scope.content = [];
  $scope.headers = [
    {
      name:'',
      field:'id'
    },{
      name:'Algorithm Name',
      field:'name'
    },{
      name: 'Date',
      field: 'date'
    },{
      name:'Execution Time (d HH:mm:ss)',
      field: 'time'
    },{
      name: 'Inputs',
      field: 'inputs'
    },{
      name:'Result Types',
      field: 'resultType'
    },{
      name: '',
      field: 'actions'
    }
  ];
  $scope.loadExecutions = loadExecutions;


  // Private varias
  var executions;

  // ** FUNCTION DEFINITIONS **
  // **************************

  function loadExecutions() {
    Executions.getAll({}, function(result) {
      executions = result;
      $scope.content = [];
      result.forEach(function(execution) {
        var duration = execution.end - execution.begin;
        if(execution.end === 0) {
          duration = 0
        }
        var inputs = [];
        var results = [];
        execution.inputs.forEach(function(input) {
            input.name = input.name.replace(/^.*[\\\/]/, '');
            inputs.push(input.name)
        });
        execution.results.forEach(function(result) {
            results.push(result.typeName)
        });
        if(execution.aborted) {
          results = ['EXECUTION ABORTED']
        }
        $scope.content.push({
          id: execution.id,
          name: execution.algorithm.name,
          date: $filter('date')(execution.begin, 'yyyy-MM-dd HH:mm:ss'),
          time: Math.floor(duration/(60*60*24))+' '+twoDigets(Math.floor(duration/(60*60)))+':'+twoDigets(Math.floor((duration/60)%60)) + ':' + twoDigets(Math.floor(duration%60)),
          inputs: inputs.join(', '),
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
        $scope.content = orderBy($scope.content, $scope.sortable[0], true);
    })
  }

    loadExecutions();

    $scope.custom = {name: 'bold', date:'grey', time: 'grey'};
    $scope.sortable = ['id', 'date', 'name', 'time', 'inputs', 'resultType'];
//    $scope.thumbs = 'thumb';
    $scope.count = 10;
    function twoDigets(number) {
      return (number < 10 ? '0'+number : ''+number)
    }
  });

app.directive('mdTable', function () {
    return {
      restrict: 'E',
      scope: {
        headers: '=',
        content: '=',
        sortable: '=',
        filters: '=',
        customClass: '=customClass',
        thumbs:'=',
        count: '='
      },
      controller: function ($scope,$filter,$window, $timeout, LoadResults, $location, Delete, ngDialog, usSpinnerService) {
        var orderBy = $filter('orderBy');
        $scope.tablePage = 0;
        $scope.nbOfPages = function () {
          return Math.ceil($scope.content.length / $scope.count);
        };
        $scope.handleSort = function (field) {
          return $scope.sortable.indexOf(field) > -1;
        };
        $scope.order = function(predicate, reverse) {
          $scope.content = orderBy($scope.content, predicate, reverse);
          $scope.predicate = predicate;
        };
        $scope.order($scope.sortable[0],true);
        $scope.getNumber = function (num) {
          return new Array(num);
        };
        $scope.goToPage = function (page) {
          $scope.tablePage = page;
        };
        $scope.showResult = function(result) {
          if (!result.aborted) {
            $location.url('/result/' + result.id + '?count=' + result.count + '&cached=' + result.cached +
            '&ind=' + result.ind + '&fd=' + result.fd + '&ucc=' + result.ucc +
            '&cucc=' + result.cucc + '&od=' + result.od + '&basicStat=' + result.basicStat);
          }
        };
        $scope.deleteExecution = function(execution) {
          Delete.execution({id: execution.id}, function() {
            $scope.$parent.loadExecutions()
          })
        };
        $scope.startSpin = function() {
          usSpinnerService.spin('spinner-1');
        };
        $scope.stopSpin = function() {
          usSpinnerService.stop('spinner-1');
        };
        $scope.confirmDelete = function(execution) {
          $scope.confirmText = 'Are you sure you want to delete it?';
          $scope.confirmItem = execution;
          $scope.confirmFunction = function () {
            $scope.startSpin();
            Delete.execution({id: $scope.confirmItem.id}, function() {
              $scope.$parent.loadExecutions()
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


      },
      template: angular.element(document.querySelector('#md-table-template')).html()
    }
  });



app.directive('mdColresize', function () {
  return {
    restrict: 'A',
    link: function () {
//      scope.$evalAsync(function () {
//        $timeout(function(){ $(element).colResizable({
//          liveDrag: true,
//          fixed: true

//        });},100);
//      });
    }
  }
});

app.filter('startFrom',function (){
  return function (input,start) {
    start = +start;
    return input.slice(start);
  }
});
