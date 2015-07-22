'use strict';

angular.module('v2')
  .controller('NewCtrl', function ($scope, $log, ngDialog, Algorithms, Datasource) {
    $scope.category = []
    $scope.datasources = []

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

    var inputCategories = [
      {
        name: "file-inputs",
        display: "File Input"
      },
      {
        name: "database-connections",
        display: "Database Connection"
      },
      {
        name: "table-inputs",
        display: "Table Inputs"
      }
    ]
    
    algorithmCategories.forEach(function(category){
      Algorithms.get({type: category.name}, function(result){
        $scope.category.push({
            name: category.display,
            algorithms: result
        })
       })
    })
     
    inputCategories.forEach(function(category){
      Datasource.get({type: category.name}, function(result){
        //Remove path from element name
        result.forEach(function(element){
          element.name = element.name.replace(/^.*[\\\/]/, '')
        })

        $scope.datasources.push({
            name: category.display,
            datasource: result,
            possible: true
        })
       })
    })

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
    $scope.activateAlgorithm = function(algorithm) {
      algorithm.active = true
      if($scope.activeAlgorithm) {
        $scope.activeAlgorithm.active = false
      }
      $scope.activeAlgorithm = algorithm
      
      $scope.datasources.forEach(function(datasource){
        if(datasource.name == "Table Inputs"){
           datasource.possible = algorithm.tableInput
        } else if(datasource.name == "Database Connection"){
           datasource.possible = algorithm.databaseConnection
        } else if(datasource.name == "File Input"){
           datasource.possible = algorithm.fileInput
        }
      })
    }

  });
