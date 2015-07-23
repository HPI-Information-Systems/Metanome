'use strict';

angular.module('v2')
  .controller('NewCtrl', function ($scope, $log, ngDialog, Algorithms, Datasource, Parameter) {
    $scope.category = []
    $scope.datasources = []

    //Params
    $scope.model = {};
    $scope.form = [];
    $scope.schema = {
      type: "object",
      properties: {}
    };

    resetParameter()

    var algorithmCategories = [
      {
        name: 'inclusion-dependency-algorithms',
        display: 'Inclusion Dependency Algorithms'
      },      
      {
        name: 'functional-dependency-algorithms',
        display: 'Functional Dependency Algorithms'
      },      
      {
        name: 'unique-column-combination-algorithms',
        display: 'Unique Column Combination Algorithms'
      },      
      {
        name: 'conditional-unique-column-combination-algorithms',
        display: 'Conditional Unique Column Combination Algorithms'
      },
      {
        name: 'order-dependency-algorithms',
        display: 'Order Dependency Algorithms'
      },
      {
        name: 'basic-statistics-algorithms',
        display: 'Basic Statistics Algorithms'
      }
    ]

    var inputCategories = [
      {
        name: 'file-inputs',
        display: 'File Input'
      },
      {
        name: 'database-connections',
        display: 'Database Connection'
      },
      {
        name: 'table-inputs',
        display: 'Table Inputs'
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
      initializeForm()
      highlightAlgorithm(algorithm)
      updateAvailableDatasources(algorithm)
      resetParameter()
      initializeForm()
      updateParameter(algorithm)
    }

    function initializeForm() {
     $scope.form = [
        "*",
        {
          type: "submit",
          title: "Execute"
        }
      ];
    }
    function highlightAlgorithm(algorithm){
      if($scope.activeAlgorithm) {
        $scope.activeAlgorithm.active = false
      }
      algorithm.active = true
      $scope.activeAlgorithm = algorithm 
    }
    function updateAvailableDatasources(algorithm) {
      $scope.datasources.forEach(function(datasource){
        if(datasource.name === 'Table Inputs'){
           datasource.possible = algorithm.tableInput
        } else if(datasource.name === 'Database Connection'){
           datasource.possible = algorithm.databaseConnection
        } else if(datasource.name === 'File Input'){
           datasource.possible = algorithm.fileInput
        }
      })
    }
    function resetParameter() {
      $scope.model = {};
      $scope.form = [];
      $scope.schema = {
        type: "object",
        properties: {}
      };
    }
    function updateParameter(algorithm){
      $scope.params = []
      Parameter.get({algorithm: algorithm.fileName}, function(parameter) {
        parameter.forEach(function(param){
          switch (param.type) {
            case 'ConfigurationRequirementFileInput':
              configureParamInputs(param, 'File Input')
              break;
            case 'ConfigurationRequirementTableInput':
              configureParamInputs(param, 'Table Inputs')
              break;
            case 'ConfigurationRequirementDatabaseConnection':
              configureParamInputs(param, 'Database Connection')
              break;
            case 'ConfigurationRequirementString':
              configureParamStrings(param)
              break
            case 'ConfigurationRequirementListBox':
              console.log(param.type)
              break;
          }
        })
      })
    }
    function configureParamInputs(param, input){
      $scope.params.push(param)
      var index = -1
      $.grep($scope.datasources, function(element, i) {
        if(element !== undefined && element.name !== undefined && element.name.indexOf(input) > -1){
          index = i
          return true
        } else {
          return false 
        }
      })
      if(param.minNumberOfSettings === param.maxNumberOfSettings) {
        $scope.datasources[index].name = input + ' (choose ' + param.minNumberOfSettings + ')'
      } else {
        $scope.datasources[index].name = input + ' (min: ' + param.minNumberOfSettings + ' | max: ' + param.maxNumberOfSettings + ')'
      }
    }
    function configureParamStrings(param) {
      $scope.params.push(param)
      var i = 0
      for (i; i < param.maxNumberOfSettings; i++){
          $scope.schema.properties[param.identifier + "-" + i] = {
            "title": param.identifier + "-" + i,
            "type": "string"
          }
          //$scope.form.splice($scope.form.length-1, 0, param.identifier + "-" + i)
      }
    }
    
  });
