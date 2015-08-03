'use strict'

angular.module('v2')

.config(function config( $stateProvider ) {
  $stateProvider
    .state('new', {
      url: '/new',
      views: {
        'main@': {
            controller: 'NewCtrl',
            templateUrl: 'app/new/new.html'
         }
      }
    })
})

.controller('NewCtrl', function (
  $scope,
  $log,
  ngDialog,
  Algorithms,
  Datasource,
  Parameter,
  AlgorithmExecution,
  usSpinnerService,
  $location,
  NewAlgorithm,
  AvailableAlgorithmFiles,
  AvailableInputFiles,
  Delete,
  StopExecution
) {

  //Exported functions
  $scope.openAlgorithmSettings = openNewAlgorithm
  $scope.openDatasourceSettings = openNewDatasource
  $scope.executeAlgorithm = executeAlgorithm
  $scope.toggleDatasource = toggleDatasource
  $scope.activateAlgorithm = activateAlgorithm
  $scope.confirmDelete = confirmDelete
  $scope.confirmDialog = confirmDialog

  //Exports for dialogs
  $scope.NewAlgorithm = NewAlgorithm
  $scope.AvailableAlgorithmFiles = AvailableAlgorithmFiles
  $scope.AvailableInputFiles = AvailableInputFiles
  $scope.StopExecution = StopExecution

  //Exported variables
  $scope.algorithms = []  // algorithm categories + algorithms
  $scope.datasources = [] // datasource
  $scope.maxNumberOfSetting = {}
  $scope.model = {} // saved params go here
  $scope.form = []  // params form
  $scope.schema = {  // schema of params section
    type: 'object',
    properties: {}
  }
  $scope.algorithmHasCustomProperties
  $scope.activeAlgorithm
  $scope.cachingSelection = 'cache'

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

  $scope.tabs = [
    {
      title: 'New',
      view: 'new'
    },
    {
      title: 'History',
      view: 'history'
    }
  ]

  // Execute initialization
  initializeAlgorithmList()
  initializeDatasources()
  resetParameter()

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

    $scope.algorithms = []

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

    $scope.datasources = []

    inputCategories.forEach(function(category){
      Datasource.get({type: category.name}, function(result){
        //Remove path from element name
        result.forEach(function(element){
          element.name = element.name.replace(/^.*[\\\/]/, '')
          element.disabled = false
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
  function openNewAlgorithm() {
    ngDialog.open({
      template: '/assets/new-algorithm.html',
      scope: $scope,
      controller: ['$scope', function($scope) {
        $scope.saveNewAlgorithm = saveNewAlgorithm
        $scope.algorithmFiles = []
        loadAvailableAlgorithms()
        function loadAvailableAlgorithms() {
            $scope.$parent.AvailableAlgorithmFiles.get(function(result) {
                $scope.$parent.algorithms.forEach(function(algorithmCategory){
                    algorithmCategory.algorithms.forEach(function(algorithm) {
                        var index = result.indexOf(algorithm.fileName);
                        if (index !== -1) {
                            result.splice(index, 1);
                        }
                    })
                })
                $scope.algorithmFiles = result
            })
        }
        function saveNewAlgorithm(algorithm) {
            console.log($scope.$parent.dialogExports)
            $scope.$parent.NewAlgorithm.store({
              "id": 1,
              "fileName": algorithm.fileName,
              "name": algorithm.name,
              "author": algorithm.author,
              "description": algorithm.description,
              "ind": true,
              "fd": false,
              "ucc": false,
              "cucc": false,
              "od": false,
              "relationalInput": false,
              "databaseConnection": false,
              "tableInput": false,
              "fileInput": true,
              "basicStat": false
          }, function() {
              initializeAlgorithmList()
          })
        }
      }]
    })
  }
  function openNewDatasource() {
    ngDialog.open({
      template: '/assets/new-datasource.html',
      scope: $scope,
      controller: ['$scope', function($scope) {
        $scope.selectDatasourceCategory = selectDatasourceCategory
        $scope.newDataSourceCategory = 'file'
        $scope.files = []
        $scope.databaseConnections = []
        loadAvailableFiles()
        loadAvailableDatasources()
        function selectDatasourceCategory(category) {
          $scope.newDataSourceCategory = category
        }
        function loadAvailableFiles() {
          $scope.AvailableInputFiles.get(function(result) {
            $scope.$parent.datasources.forEach(function(category){
              if (category.name == 'File Input') {
                category.datasource.forEach(function(file){
                  var index = result.indexOf(file.fileName);
                  if (index !== -1) {
                    result.splice(index, 1);
                  }
                })
              }
            })
            $scope.files = result
          })
        }
        function loadAvailableDatasources() {
          $scope.$parent.datasources.forEach(function (category) {
            if (category.name == 'Database Connection') {
              $scope.databaseConnections = category.datasource
            }
          })
        }
      }]
    })
  }
  function confirmDelete(item) {
    $scope.confirmText = "Are you sure you want to delete it?"
    $scope.confirmItem = item
    $scope.confirmFuntion = function(){
      switch ($scope.confirmItem.type) {
        case 'fileInput':
          Delete.file({id: $scope.confirmItem.id}, function(){
            initializeDatasources()
            ngDialog.closeAll()
          })
          break
        case 'databaseConnection':
          Delete.database({id: $scope.confirmItem.id}, function(){
            initializeDatasources()
            ngDialog.closeAll()
          })
          break
        case 'tableInput':
          Delete.table({id: $scope.confirmItem.id}, function(){
            initializeDatasources()
            ngDialog.closeAll()
          })
          break
        default:
          Delete.algorithm({id: $scope.confirmItem.id}, function(){
            initializeAlgorithmList()
            ngDialog.closeAll()
          })
          break
      }
    }
    openConfirm()
  }
  function openConfirm() {
    ngDialog.openConfirm({
      template:'\
                <h3>Confirm</h3>\
                <p>{{$parent.confirmText}}</p>\
                <div class="ngdialog-buttons">\
                    <button type="button" class="ngdialog-button ngdialog-button-secondary" ng-click="closeThisDialog(0)">No</button>\
                    <button type="button" class="ngdialog-button ngdialog-button-warning" ng-click="$parent.confirmDialog(1)">Yes</button>\
                </div>',
      plain: true,
      scope: $scope
    })
  }
  function confirmDialog() {
    $scope.confirmFuntion()
  }

  // Actions
  function toggleDatasource(datasource){
    var index = activeDataSources[datasource.type].indexOf(datasource.id)
    datasource.active = datasource.active || false
    datasource.active = !datasource.active

    console.log(activeDataSources)

    if (index === -1) {
      activeDataSources[datasource.type].push(datasource.id)
    } else {
      activeDataSources[datasource.type].splice(index, 1)
      enableAllInputs()
    }

    var type = ''
    if(datasource.type == 'fileInput'){
      type = 'File Input'
    } else if(datasource.type == 'databaseConnection'){
      type = 'Database Connection'
    } else if(datasource.type == 'tableInput'){
      type = 'Table Inputs'
    }
    //
    //console.log($scope.maxNumberOfSetting)
    //console.log($scope.maxNumberOfSetting[type] + '/' + activeDataSources[datasource.type].length)

    if($scope.maxNumberOfSetting[type] <= activeDataSources[datasource.type].length) {
      $scope.datasources.forEach(function(ds) {
        if(ds.name.indexOf(type) > -1) {
          ds.datasource.forEach(function(element) {
            if(element.active === undefined || element.active == false){
              element.disabled = true
            }
          })
        }
      })
    } else {
      enableAllInputs()
    }
  }
  function activateAlgorithm(algorithm) {
    highlightAlgorithm(algorithm)
    updateAvailableDatasources(algorithm)
    enableAllInputs()
    unselectAllInputs()
    resetParameter()
    initializeForm()
    updateParameter(algorithm)
  }
  function executeAlgorithm(caching, memory){
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
      'cacheResults':(caching == 'cache'),
      'writeResults':(caching == 'disk'),
      'countResults':(caching == 'count'),
      'memory':memory || ''
    }
    console.log(payload)
    $scope.payload = payload
    $scope.canceled = false
    $scope.cancelFunction = function(){
      $scope.canceled = true
    }
    ngDialog.openConfirm({
      template: '\
                <h3>Execution running</h3>\
                <timer interval="1000">Elapsed time: {{days}} days, {{hours}} hour{{hoursS}}, {{minutes}} minute{{minutesS}}, {{seconds}} second{{secondsS}}.</timer>\
                <div class="ngdialog-buttons">\
                    <button type="button" class="ngdialog-button ngdialog-button-warning" ng-click="cancelExecution()">Cancel Execution</button>\
                </div>',
      plain: true,
      scope: $scope,
      controller: ['$scope', function($scope) {
        $scope.cancelExecution = function(){
          $scope.$parent.cancelFunction()
          var params = {identifier : $scope.$parent.payload.executionIdentifier}
          $scope.$parent.StopExecution.stop(params, function(){
            ngDialog.closeAll()
          })
        }
      }]
    })
    AlgorithmExecution.run({}, payload, function(result) {
      ngDialog.closeAll()
      if(!$scope.canceled) {
        if(caching = 'cache') {
          $location.url('/result/' + result.id + '?cached=true');
        } else {
          $location.url('/result/' + result.id);
        }
      }
    }, function(error){
      alert("Error!")
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
      console.log(datasource.name)
      console.log(algorithm)
      if(datasource.name.indexOf('Table Inputs') > -1){
        datasource.possible = algorithm.tableInput || algorithm.relationalInput
      } else if(datasource.name.indexOf('Database Connection') > -1){
        datasource.possible = algorithm.databaseConnection
      } else if(datasource.name.indexOf('File Input') > -1){
        datasource.possible = algorithm.fileInput || algorithm.relationalInput
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
        ]
      }
    ]
  }
  function updateParameter(algorithm){
    currentParameter = []
    $scope.maxNumberOfSetting = {}
    activeDataSources = {
      'fileInput': [],
      'tableInput': [],
      'databaseConnection': []
    }
    Parameter.get({algorithm: algorithm.fileName}, function(parameter) {
      parameter.forEach(function(param){
        currentParameter.push(param)
        switch (param.type) {
          case 'ConfigurationRequirementRelationalInput':
            configureParamInputs(param, 'File Input')
            configureParamInputs(param, 'Table Inputs')
          break
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
            addParamToList(param, 'integer', false)
          break
          case 'ConfigurationRequirementListBox':
            addParamToList(param, 'string', true)
          break
          case 'ConfigurationRequirementString':
            addParamToList(param, 'string', false)
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
    $scope.maxNumberOfSetting[input] = $scope.maxNumberOfSetting[input] || 0
    $scope.maxNumberOfSetting[input] += param.maxNumberOfSettings
  }
  function addParamToList(param, type, dropdown) {
    $scope.algorithmHasCustomProperties = true
    var i = 0
    if(param.maxNumberOfSettings > 1){
      for (i; i < param.maxNumberOfSettings; i++){
        $scope.schema.properties[param.identifier + '-' + i] = {
          'title': param.identifier + '-' + i,
          'type': type
        }
        if(dropdown){
          $scope.schema.properties[param.identifier + '-' + i].enum = param.values
        }
      }
    } else {
      $scope.schema.properties[param.identifier] = {
        'title': param.identifier,
        'type': type
      }
      if(dropdown){
        $scope.schema.properties[param.identifier].enum = param.values
      }
    }
  }
  function addDropdownToList(param) {
    $scope.schema.properties[param.identifier] = {
      'title': param.identifier,
      'type': type
    }
  }

  // Other Helpers
  function startSpin(){
    usSpinnerService.spin('spinner-1');
  }
  function stopSpin(){
    usSpinnerService.stop('spinner-1');
  }
  function twoDigetDate(number) {
    return (number < 10 ? '0'+number : ''+number)
  }
  function loadResultsForFileInput() {
    LoadResults
  }
  function unselectAllInputs() {
    [
      'File Input',
      'Database Connection',
      'Table Inputs'
    ].forEach(function(type) {
          $scope.datasources.forEach(function (ds) {
            ds.datasource.forEach(function (element) {
              element.active = false
            })
          })
        })
  }
  function enableAllInputs() {
    [
      'File Input',
      'Database Connection',
      'Table Inputs'
    ].forEach(function(type) {
          $scope.datasources.forEach(function (ds) {
            ds.datasource.forEach(function (element) {
              element.disabled = false
            })
          })
        })
  }
  function readParamsIntoBackendFormat(params){
    var i, j
    for(i=0; i < params.length; i++) {
      params[i].settings = []
      //needed because same fields vary in different places in backend - workaround!
      if(params[i].fixNumberOfSettings !== undefined) {
        delete params[i].fixNumberOfSettings
      }
      switch(params[i].type) {
        case 'ConfigurationRequirementInteger':
          if(params[i].maxNumberOfSettings > 1){
          //order seems to be from last to first in Java UI V1
          for(j=params[i].maxNumberOfSettings-1; j >= 0; j--){
            params[i].settings.push({
              'type':'ConfigurationSettingInteger',
              'value': $scope.model[params[i].identifier+'-'+j]
            })
          }
        } else {
          params[i].settings.push({
            'type':'ConfigurationSettingInteger',
            'value': $scope.model[params[i].identifier]
          })
        }
        break
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
        case 'ConfigurationRequirementListBox':
          if(params[i].maxNumberOfSettings > 1){
          //order seems to be from last to first in Java UI V1
          for(j=params[i].maxNumberOfSettings-1; j >= 0; j--){
            params[i].settings.push({
              'type':'ConfigurationSettingListBox',
              'value': $scope.model[params[i].identifier+'-'+j]
            })
          }
        } else {
          params[i].settings.push({
            'type':'ConfigurationSettingListBox',
            'value': $scope.model[params[i].identifier]
          })
        }
        break
         case 'ConfigurationRequirementTableInput':
          //order seems to be from last to first in Java UI V1
          var checked = activeDataSources.tableInput.slice(0)
        for(j=0; j < params[i].maxNumberOfSettings && checked.length > 0; j++){
          var item = dataSources.tableInput[''+checked.pop()]
          //needed because same fields are named different in different places in backend - workaround!
          var param = {
            "table": item.tableName,
            "databaseConnection":{
              "dbUrl":item.databaseConnection.url,
              "username":item.databaseConnection.username,
              "password":item.databaseConnection.password,
              "system":item.databaseConnection.system,
              "type":"ConfigurationSettingDatabaseConnection",
              "id":item.databaseConnection.id
            },
            "type":"ConfigurationSettingTableInput",
            "id":item.id
          }
          params[i].settings.push(param)
        }
        break
        case 'ConfigurationRequirementDatabaseConnection':
          //order seems to be from last to first in Java UI V1
          var checked = activeDataSources.databaseConnection.slice(0)
        for(j=0; j < params[i].maxNumberOfSettings && checked.length > 0; j++){
          var item = dataSources.databaseConnection[''+checked.pop()]
          //needed because same fields are named different in different places in backend - workaround!
          var param = {
            "dbUrl":item.url,
            "username":item.username,
            "password":item.password,
            "system":item.system,
            "type":"ConfigurationSettingDatabaseConnection",
            "id":item.id
          }
          params[i].settings.push(param)
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
