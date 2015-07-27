'use strict'

angular.module('v2')
  .controller('NewCtrl', function (
    $scope,
    $log,
    ngDialog,
    Algorithms,
    Datasource,
    Parameter,
    AlgorithmExecution
  ) {

    //Exported functions
    $scope.openAlgorithmSettings = openAlgorithmSettings
    $scope.openDatasourceSettings = openDatasourceSettings
    $scope.openNewDatasource = openNewDatasource
    $scope.executeAlgorithm = executeAlgorithm
    $scope.toggleDatasource = toggleDatasource
    $scope.activateAlgorithm = activateAlgorithm

    //Exported variables
    $scope.algorithms = []  // algorithm categories + algorithms
    $scope.datasources = [] // datasource
    $scope.model = {} // saved params go here
    $scope.form = []  // params form
    $scope.schema = {  // schema of params section
      type: 'object',
      properties: {}
    }
    $scope.algorithmHasCustomProperties
    $scope.activeAlgorithm

    //Private variables
    var activeDataSources = {
      'fileInput': [],
      'tableInput': [],
      'databaseConnection': []
     }
    var dataSources = {
      'fileInput': {},
      'tableInput': {},
      'databaseConnection': {}
     }
    var currentParameter


    // Execute initialization
    initializeAlgorithmList()
    initializeDatasources()
    resetParameter()

    // **************************
    // ** FUNCTION DEFINITIONS **
    // **************************

    // Initialization
    function initializeAlgorithmList() {
      var algorithmCategoryNames = [
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

      algorithmCategoryNames.forEach(function(category){
        Algorithms.get({type: category.name}, function(result){
          $scope.algorithms.push({
              name: category.display,
              algorithms: result
          })
         })
      })
    }
    function initializeDatasources(){
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

      inputCategories.forEach(function(category){
        Datasource.get({type: category.name}, function(result){
          //Remove path from element name
          result.forEach(function(element){
            element.name = element.name.replace(/^.*[\\\/]/, '')
            dataSources[element.type][''+element.id] = element
          })
          $scope.datasources.push({
              name: category.display,
              datasource: result,
              possible: true
          })
         })
      })
    }

    // Open Dialogs
    function openAlgorithmSettings() {
      ngDialog.open({
        template: '/assets/settings-algorithm.html',
        scope: $scope
      })
    }
    function openDatasourceSettings() {
      ngDialog.open({
        template: '/assets/settings-datasource.html',
        scope: $scope
      })
    }
    function openNewDatasource() {
      ngDialog.open({
        template: '/assets/new-datasource.html',
        scope: $scope
      })
    }

    // Actions
    function toggleDatasource(datasource){
      var index = activeDataSources[datasource.type].indexOf(datasource.id)

      if (index === -1) {
          activeDataSources[datasource.type].push(datasource.id)
      } else {
          activeDataSources[datasource.type].splice(datasource.id, 1)
      }
    }
    function activateAlgorithm(algorithm) {
      highlightAlgorithm(algorithm)
      updateAvailableDatasources(algorithm)
      resetParameter()
      initializeForm()
      updateParameter(algorithm)
    }
    function executeAlgorithm(){
      var algorithm = $scope.activeAlgorithm
      var params = readParamsIntoBackendFormat(currentParameter)
      var date = new Date()
      var executionIdentifierDate = date.getFullYear()+'-'+twoDigetDate(date.getMonth()+1)+'-' +
                                       twoDigetDate(date.getDate())+'T'+twoDigetDate(date.getHours()) +
                                       twoDigetDate(date.getMinutes())+twoDigetDate(date.getSeconds())
      var payload =  {
         'algorithmId':algorithm.id,
         'executionIdentifier':algorithm.fileName+executionIdentifierDate ,
         'requirements': params,
         'cacheResults':true,
         'writeResults':false,
         'countResults':false,
         'memory':''
      }
      AlgorithmExecution.run({}, payload, function(result) {

      })
    }

    // Activate Algorithm Helpers
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
      $scope.model = {}
      $scope.form = []
      $scope.schema = {
        type: 'object',
        properties: {}
      }
      $scope.algorithmHasCustomProperties = false
    }
    function initializeForm() {
     $scope.form = [
       '*',
       {
          'type': 'actions',
          'items': [
           {
              'type': 'button',
              'title': 'Execute',
              'onClick': 'executeAlgorithm()'
            }
          ]
        }
     ]
    }
    function updateParameter(algorithm){
      currentParameter = []
      Parameter.get({algorithm: algorithm.fileName}, function(parameter) {
        parameter.forEach(function(param){
          switch (param.type) {
            case 'ConfigurationRequirementFileInput':
              configureParamInputs(param, 'File Input')
              break
            case 'ConfigurationRequirementTableInput':
              configureParamInputs(param, 'Table Inputs')
              break
            case 'ConfigurationRequirementDatabaseConnection':
              configureParamInputs(param, 'Database Connection')
              break
            case 'ConfigurationRequirementInteger':
              addParamToList(param, 'integer')
              break
             case 'ConfigurationRequirementString':
              addParamToList(param, 'string')
              break
            default:
              console.error("Parameter type " + param.type + " not supported yet")
              break
          }
        })
      })
    }

    // Parameter Helpers
    function configureParamInputs(param, input){
      currentParameter.push(param)
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
    function addParamToList(param, type) {
      currentParameter.push(param)
      $scope.algorithmHasCustomProperties = true
      var i = 0
      if(param.maxNumberOfSettings > 1){
        for (i; i < param.maxNumberOfSettings; i++){
            $scope.schema.properties[param.identifier + '-' + i] = {
              'title': param.identifier + '-' + i,
              'type': type
            }
        }
      } else {
            $scope.schema.properties[param.identifier] = {
              'title': param.identifier,
              'type': type
            }
      }
    }

    // Other Helpers
    function twoDigetDate(number) {
      return (number < 10 ? '0'+number : ''+number)
    }
    function readParamsIntoBackendFormat(params){
      var i, j
      for(i=0; i < params.length; i++) {
        params[i].settings = []
          switch(params[i].type) {
            case 'ConfigurationRequirementString':
              if(params[i].maxNumberOfSettings > 1){
                //order seems to be from last to first in Java UI V1
                for(j=params[i].maxNumberOfSettings-1; j >= 0; j--){
                  params[i].settings.push({
                    'type':'ConfigurationSettingString',
                    'value': $scope.model[params[i].identifier+'-'+j]
                  })
                }
              } else {
                  params[i].settings.push({
                    'type':'ConfigurationSettingString',
                    'value': $scope.model[params[i].identifier]
                  })
              }
            break
            case 'ConfigurationRequirementFileInput':
                //order seems to be from last to first in Java UI V1
                var checked = activeDataSources.fileInput.slice(0)
                for(j=0; j < params[i].maxNumberOfSettings && checked.length > 0; j++){
                  var item = dataSources.fileInput[''+checked.pop()]
                  //needed because same fields are named different in different places in backend - workaround!
                  var param = {
                     'fileName':item.fileName,
                     'advanced':false,
                     'separatorChar':item.separator,
                     'quoteChar':item.quoteChar,
                     'escapeChar':item.escapeChar,
                     'strictQuotes':item.strictQuotes,
                     'ignoreLeadingWhiteSpace':item.ignoreLeadingWhiteSpace,
                     'skipLines':item.skipLines,
                     'header':item.hasHeader,
                     'skipDifferingLines':item.skipDifferingLines,
                     'nullValue':item.nullValue,
                     'type':'ConfigurationSettingFileInput',
                     'id':item.id
                  }
                   params[i].settings.push(param)
                   //needed because same fields different in different places in backend - workaround!
                   delete params[i].fixNumberOfSettings
                }
            break
            default:
              console.error('Parameter Type '+params[i].type+' not not supported yet for execution!')
              break
          }
        }
      return params
    }
  })
