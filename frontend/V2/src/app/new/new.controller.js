'use strict';

angular.module('v2')
  .controller('NewCtrl', function ($scope, $log, ngDialog, Algorithms) {
    $scope.category = []

    var algorithmCategories = [
      {
        name: "inclusion-dependency-algorithms",
        display: "Inclusion Dependency Algorithms"
      },      
      {
        name: "functional-dependency-algorithms",
        display: "Functional Dependency Algorithms"
      },      
      {
        name: "unique-column-combination-algorithms",
        display: "Unique Column Combination Algorithms"
      },      
      {
        name: "conditional-unique-column-combination-algorithms",
        display: "Conditional Unique Column Combination Algorithms"
      },
      {
        name: "order-dependency-algorithms",
        display: "Order Dependency Algorithms"
      },
      {
        name: "basic-statistics-algorithms",
        display: "Basic Statistics Algorithms"
      }
    ]
    
    algorithmCategories.forEach(function(category){
      Algorithms.get({type: category.name}, function(result){
        console.log(result[0])
        $scope.category.push({
            name: category.display,
            algorithms: result
        })
       })
    })

    $scope.datasources = [
      {
        name: 'File input',
        datasource: [
          {
            name: 'File 1',
            desc: 'Example description',
          },
          {
            name: 'File 2',
            desc: 'Example description',
          },
          {
            name: 'File 3',
            desc: 'Example description'
          }
        ]
      },
      {
        name: 'Database connection',
        datasource: [
          {
            name: 'DB1',
            desc: 'Example description',
          }
        ]
      },
      {
        name: 'Table input',
        datasource: [
          {
            name: 'Super Table',
            desc: 'Example description',
          }
        ]
      }
    ]
    $scope.openAlgorithmSettings = function() {
      ngDialog.open({ 
        template: '/assets/settings-algorithm.html',
        scope: $scope
      });
    };    
    $scope.openDatasourceSettings = function() {
      ngDialog.open({ 
        template: '/assets/settings-datasource.html',
        scope: $scope
      });
    };
    $scope.openNewDatasource = function() {
      ngDialog.open({ 
        template: '/assets/new-datasource.html',
        scope: $scope
      });
    };

  });
