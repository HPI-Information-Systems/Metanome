'use strict';

var app = angular.module('v2')
 
app.controller('HistoryCtrl', function ($scope, $log, Executions, $filter) {

  // Public variables
  $scope.toggleSearch = false;   
  $scope.content = []
  $scope.headers = [
    {
      name:'Algorithm Name',
      field:'name'
    },{
      name: 'Date', 
      field: 'date'
    },{
      name:'Execution Time (HH:mm:ss)', 
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

  // Private variables
  var executions

  loadExecutions()

  // ** FUNCTION DEFINITIONS **
  // **************************

  function loadExecutions() {
    Executions.getAll({}, function(result) {
      executions = result
      $scope.content = []
      result.forEach(function(execution) {
        $scope.content.push({
          name: execution.algorithm.name,
          date: $filter('date')(execution.begin, 'yyyy-MM-dd HH:mm:ss'),
          time: 'time',
          inputs: 'inputs',
          resultType: 'resultType',
          actions: ''
        })
      })
    })
  }
   
    $scope.custom = {name: 'bold', date:'grey', time: 'grey'};
    $scope.sortable = ['date', 'name', 'time', 'inputs', 'resultType'];
//    $scope.thumbs = 'thumb';
    $scope.count = 10;
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
      controller: function ($scope,$filter,$window) {
        var orderBy = $filter('orderBy');
        $scope.tablePage = 0;
        $scope.nbOfPages = function () {
          return Math.ceil($scope.content.length / $scope.count);
        },
          $scope.handleSort = function (field) {
            if ($scope.sortable.indexOf(field) > -1) { return true; } else { return false; }
        };
        $scope.order = function(predicate, reverse) {
            $scope.content = orderBy($scope.content, predicate, reverse);
            $scope.predicate = predicate;
        };
        $scope.order($scope.sortable[0],false);
        $scope.getNumber = function (num) {
                              return new Array(num);
        };
        $scope.goToPage = function (page) {
          $scope.tablePage = page;
        };
      },
      template: angular.element(document.querySelector('#md-table-template')).html()
    }
  });

app.directive('mdColresize', function ($timeout) {
  return {
    restrict: 'A',
    link: function (scope, element, attrs) {
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
