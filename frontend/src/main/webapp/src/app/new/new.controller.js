'use strict';

angular.module('Metanome')

  .config(function config($stateProvider) {
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

  .controller('NewCtrl', function ($scope,
                                   $log,
                                   ngDialog,
                                   Algorithms,
                                   Datasource,
                                   Parameter,
                                   AlgorithmExecution,
                                   usSpinnerService,
                                   $location,
                                   InputStore,
                                   AvailableAlgorithmFiles,
                                   AvailableInputFiles,
                                   Delete,
                                   StopExecution
) {

    // ** VARIABLE DEFINITIONS **
    // **************************

    //Exported variables
    $scope.algorithms = [];  // algorithm categories + algorithms
    $scope.datasources = []; // datasource
    $scope.maxNumberOfSetting = {};
    $scope.model = {}; // saved params go here
    $scope.form = [];  // params form
    $scope.schema = {  // schema of params section
      type: 'object',
      properties: {},
      required: [],
      form: []
    };
    $scope.algorithmHasCustomProperties = false;
    $scope.activeAlgorithm = undefined;
    $scope.cachingSelection = 'cache';
    $scope.saveUpdateButton = 'Save';

    //Private variables
    var activeDataSources = {
      'fileInput': [],
      'tableInput': [],
      'databaseConnection': []
    };
    var dataSources = {
      'fileInput': {},
      'tableInput': {},
      'databaseConnection': {}
    };
    var currentParameter;

    // ** FUNCTION DEFINITIONS **
    // **************************


    // ***
    // Initialization
    // ***

    /**
     * Initialize the algorithm list.
     * The algorithms for each category are loaded from the backend, sorted and stored.
     */
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
          name: 'multivalued-dependency-algorithms',
          display: 'Multivalued Dependency Algorithms'
          },
        {
          name: 'basic-statistics-algorithms',
          display: 'Basic Statistics Algorithms'
        }
      ];

      $scope.algorithms = [];

      // Get all algorithms for each category from the database
      // and sort them alphabetically
      algorithmCategoryNames.forEach(function (category) {
        Algorithms.get({type: category.name}, function (result) {
          result = removeDuplicates(result);
          $scope.algorithms.push({
            name: category.display,
            algorithms: result.sort(function (a, b) {
              return a.name.localeCompare(b.name)
            })
          });
          $scope.algorithms.sort(function (a, b) {
            return a.name.localeCompare(b.name)
          })
        })
      });

    }

    /**
     * Initialize the data source list.
     * The data sources for each category are loaded from the backend, sorted and stored.
     */
    function initializeDatasources() {
      var inputCategories = [
        {
          name: 'file-inputs',
          display: 'File Input',
          order: 1
        },
        {
          name: 'database-connections',
          display: 'Database Connection',
          order: 2
        },
        {
          name: 'table-inputs',
          display: 'Table Inputs',
          order: 3
        }
      ];

      $scope.datasources = [];
      var usedDatabases = [];

      // Get all data sources for each category from the database
      // and sort them alphabetically
      inputCategories.forEach(function (category) {
        Datasource.get({type: category.name}, function (result) {
          result = removeDuplicates(result);
          result.forEach(function (element) {
            //Remove path from element name
            if (category.name === 'file-inputs') {
              element.name = element.name.replace(/^.*[\\\/]/, '');
            }
            if (category.name === 'database-connections') {
              element.name = element.url + '; ' + element.username + '; ' + element.system;
            }
            if (category.name === 'table-inputs') {
              usedDatabases.push(element.databaseConnection.id);
              element.databaseConnection.name = element.databaseConnection.url + '; ' + element.databaseConnection.username + '; ' + element.databaseConnection.system;
            }
            element.disabled = false;
            dataSources[element.type]['' + element.id] = element;
          });
          $scope.datasources.push({
            name: category.display,
            order: category.order,
            datasource: result.sort(function (a, b) {
              return a.name.localeCompare(b.name)
            }),
            possible: true
          });
          $scope.datasources.sort(function (a, b) {
            return a.order - b.order
          });

          // when all datasources are set
          if ($scope.datasources.length === 3) {
            // we know that the database connections are at position 2,
            // because of our ordering
            $scope.datasources[1].datasource.forEach(function (db) {
              // set database, which are used by a table input, to 'used'
              // so that they can not be deleted
              if (usedDatabases.indexOf(db.id) > -1) {
                db.used = true
              }
            });
          }
        })
      });
    }


    // ***
    // Dialogs
    // ***

    /**
     * Opens a dialog for editing or adding an algorithm.
     */
    function openNewAlgorithm() {
      ngDialog.open({
        template: '/assets/new-algorithm.html',
        scope: $scope,
        preCloseCallback: function () {
          doneEditingAlgorithm()
        },
        controller: ['$scope', function ($scope) {

          // *** Variable Definitions ***

          if ($scope.$parent.AlgorithmToEdit) {
            $scope.newAlgorithm = $scope.$parent.AlgorithmToEdit;
            $scope.defaultAlgorithmText = $scope.newAlgorithm.fileName;
          } else {
            $scope.newAlgorithm = {};
            $scope.defaultAlgorithmText = '--choose an algorithm--';
            $scope.newAlgorithm.author = 'No author';
            $scope.newAlgorithm.description = 'No description';
          }
          $scope.algorithmFiles = [];

          // *** Function Definitions ***

          // Loads the available algorithm jars
          function loadAvailableAlgorithms() {
            $scope.$parent.AvailableAlgorithmFiles.get(function (result) {
              $scope.$parent.algorithms.forEach(function (algorithmCategory) {
                algorithmCategory.algorithms.forEach(function (algorithm) {
                  var index = result.indexOf(algorithm.fileName);
                  if (index !== -1) {
                    result.splice(index, 1);
                  }
                })
              });
              $scope.algorithmFiles = result;
              if ($scope.$parent.AlgorithmToEdit) {
                $scope.algorithmFiles.push($scope.newAlgorithm.fileName);
              }
            });
          }

          // Save or update a algorithm
          function saveNewAlgorithm(algorithm) {
            resetAlgorithm();
            if (!algorithm.fileName) {
              openError('You have to select an algorithm jar-file!');
              return;
            }
            if (!algorithm.name) {
              openError('You have to insert an algorithm name!');
              return;
            }
            startSpin();
            var obj = {
              'id': algorithm.id,
              'fileName': algorithm.fileName,
              'name': algorithm.name,
              'author': algorithm.author,
              'description': algorithm.description,
              'ind': algorithm.ind,
              'fd': algorithm.fd,
              'ucc': algorithm.ucc,
              'cucc': algorithm.cucc,
              'od': algorithm.od,
              'mvd': algorithm.mvd,
              'basicStat': algorithm.basicStat,
              'relationalInput': algorithm.relationalInput,
              'databaseConnection': algorithm.databaseConnection,
              'tableInput': algorithm.tableInput,
              'fileInput': algorithm.fileInput
            };
            if ($scope.$parent.AlgorithmToEdit) {
              $scope.$parent.InputStore.updateAlgorithm(obj, function () {
                stopSpin();
                initializeAlgorithmList();
                ngDialog.closeAll()
              }, function (errorMessage) {
                stopSpin();
                openError('An error occurred when updating this algorithm: ' + errorMessage.data)
              })
            }
            else {
              $scope.$parent.InputStore.newAlgorithm(obj, function () {
                stopSpin();
                initializeAlgorithmList();
                ngDialog.closeAll()
              }, function (errorMessage) {
                stopSpin();
                openError('An error occurred when saving this algorithm: ' + errorMessage.data)
              })
            }
          }

          // Updates author and description of an algorithm, if another algorithm is selected in the dropdown
          function algorithmFileChanged() {
            Parameter.authorsDescription({algorithm: $scope.newAlgorithm.fileName}, function (data) {
              if (data.authors === undefined || !data.authors) {
                $scope.newAlgorithm.author = 'No author';
              } else {
                $scope.newAlgorithm.author = data.authors;
              }

              if (data.description === undefined || !data.description) {
                $scope.newAlgorithm.description = 'No description';
              } else {
                $scope.newAlgorithm.description = data.description;
              }

            })
          }

          // *** Export functions ***

          $scope.saveNewAlgorithm = saveNewAlgorithm;
          $scope.algorithmFileChanged = algorithmFileChanged;

          // *** Function Calls ***

          loadAvailableAlgorithms();

        }]
      })
    }

    /**
     * Opens a dialog for editing or adding data sources.
     */
    function openNewDatasource() {
      ngDialog.open({
        template: '/assets/new-datasource.html',
        scope: $scope,
        preCloseCallback: function () {
          doneEditingDatasources()
        },
        controller: ['$scope', function ($scope) {

          // *** Variable Definitions ***

          $scope.newDataSourceCategory = 'file';

          if ($scope.$parent.editFileInput) {
            $scope.file = $scope.$parent.editFileInput;
            $scope.defaultFileText = $scope.file.fileName.replace(/^.*[\\\/]/, '');
            $scope.newDataSourceCategory = 'file'
          } else {
            $scope.defaultFileText = '--choose a file--';
            $scope.file = {
              'separator': ',',
              'quoteChar': '\'',
              'escapeChar': '\\',
              'skipLines': '0',
              'strictQuotes': false,
              'ignoreLeadingWhiteSpace': true,
              'hasHeader': true,
              'skipDifferingLines': false,
              'nullValue': ''
            }
          }

          if ($scope.$parent.editDatabaseInput) {
            $scope.database = $scope.$parent.editDatabaseInput;
            $scope.newDataSourceCategory = 'database'
          } else {
            $scope.database = {}
          }

          if ($scope.$parent.editTableInput) {
            $scope.table = $scope.$parent.editTableInput;
            $scope.newDataSourceCategory = 'table';
            $scope.defaultDatabaseConnectionText = $scope.table.databaseConnection.name;
          } else {
            $scope.table = {};
            $scope.defaultDatabaseConnectionText = '--choose a database connection--';
          }

          $scope.files = [];
          $scope.databaseConnections = [];

          // *** Function Defintions ***

          // Sets the selected data source category (file input, table input, database connection)
          function selectDatasourceCategory(category) {
            $scope.newDataSourceCategory = category
          }

          // Loads the available files on disk
          function loadAvailableFiles() {
            $scope.AvailableInputFiles.get(function (result) {
              var updatedResult = result.map(function(f) {return f.replace(/^.*[\\\/]/, '')});
              $scope.$parent.datasources.forEach(function (category) {
                if (category.name === 'File Input') {
                  category.datasource.forEach(function (file) {
                    var index = updatedResult.indexOf(file.fileName.replace(/^.*[\\\/]/, ''));
                    if (index !== -1) {
                      result.splice(index, 1);
                      updatedResult.splice(index, 1);
                    }
                  })
                }
              });
              result.forEach(function (file) {
                $scope.files.push({
                  fileName: file,
                  shortFileName: file.replace(/^.*[\\\/]/, '')
                })
              });
              if ($scope.$parent.editFileInput) {
                $scope.files.push({
                  fileName: $scope.file.fileName,
                  shortFileName: $scope.file.fileName.replace(/^.*[\\\/]/, '')
                });
              }
              $scope.files.sort(function (a, b) {
                return a.shortFileName.localeCompare(b.shortFileName);
              });
            })
          }

          // Loads the available database connections
          function loadAvailableDatabases() {
            $scope.$parent.datasources.forEach(function (category) {
              if (category.name === 'Database Connection') {
                $scope.databaseConnections = category.datasource
              }
            });
          }

          // Save or update a file input
          function saveNewFileInput(file) {
            resetAlgorithm();
            if (!file.fileName) {
              openError('You have to select a file!');
              return;
            }
            startSpin();
            var obj = {
              'type': 'fileInput',
              'id': file.id || 1,
              'name': file.fileName || '',
              'fileName': file.fileName || '',
              'separator': file.separator || '',
              'quoteChar': file.quoteChar || '',
              'escapeChar': file.escapeChar || '',
              'skipLines': file.skipLines || '0',
              'strictQuotes': file.strictQuotes || false,
              'ignoreLeadingWhiteSpace': file.ignoreLeadingWhiteSpace || false,
              'hasHeader': file.hasHeader || false,
              'skipDifferingLines': file.skipDifferingLines || false,
              'comment': file.comment || '',
              'nullValue': file.nullValue || ''
            };
            if ($scope.$parent.editFileInput) {
              $scope.$parent.InputStore.updateFileInput(obj, function () {
                initializeDatasources();
                ngDialog.closeAll();
                stopSpin();
              }, function (errorMessage) {
                openError('An error occurred when updating this datasource: ' + errorMessage.data);
                stopSpin();
              })
            } else {
              $scope.$parent.InputStore.newFileInput(obj, function () {
                initializeDatasources();
                ngDialog.closeAll();
                stopSpin();
              }, function (errorMessage) {
                openError('An error occurred when saving this datasource: ' + errorMessage.data);
                stopSpin();
              })
            }
          }

          // Save or update a database connection
          function saveDatabaseInput(database) {
            resetAlgorithm();
            if (!database.url) {
              openError('You have to insert a database url!');
              return;
            }
            if (!database.password) {
              openError('You have to insert a password!');
              return;
            }
            if (!database.username) {
              openError('You have to insert a username!');
              return;
            }
            if (!database.system) {
              openError('You have to select a system!');
              return;
            }
            startSpin();
            var obj = {
              'type': 'databaseConnection',
              'id': database.id || 1,
              'name': database.url + '; ' + database.userName + '; ' + database.system || '',
              'url': database.url || '',
              'username': database.username || '',
              'password': database.password || '',
              'system': database.system || '',
              'comment': database.comment || ''
            };
            if ($scope.$parent.editDatabaseInput) {
              $scope.$parent.InputStore.updateDatabaseConnection(obj,
                function () {
                  initializeDatasources();
                  ngDialog.closeAll();
                  stopSpin();
                },
                function (errorMessage) {
                  openError('An error occurred when updating this datasource: ' + errorMessage.data);
                  stopSpin();
                })
            } else {
              $scope.$parent.InputStore.newDatabaseConnection(obj,
                function () {
                  initializeDatasources();
                  ngDialog.closeAll();
                  stopSpin();
                },
                function (errorMessage) {
                  openError('An error occurred when saving this datasource: ' + errorMessage.data);
                  stopSpin();
                })
            }
          }

          // Save or update a table input
          function saveTableInput(table) {
            resetAlgorithm();
            table.databaseConnection = JSON.parse(table.databaseConnection);
            if (table.databaseConnection === undefined) {
              openError('You have to select a database connection!');
              return;
            }

            if (!table.tableName) {
              openError('You have to insert a table name!');
              return;
            }
            startSpin();
            var obj = {
              'type': 'tableInput',
              'id': table.id || 1,
              'name': table.tableName + '; ' + table.databaseConnection.name || '',
              'tableName': table.tableName || '',
              'databaseConnection': {
                'type': 'databaseConnection',
                'id': table.databaseConnection.id,
                'name': table.databaseConnection.name,
                'url': table.databaseConnection.url,
                'username': table.databaseConnection.username,
                'password': table.databaseConnection.password,
                'system': table.databaseConnection.system,
                'comment': table.databaseConnection.comment
              },
              'comment': table.comment || ''
            };
            if ($scope.$parent.editTableInput) {
              $scope.$parent.InputStore.updateTableInput(obj,
                function () {
                  initializeDatasources();
                  stopSpin();
                  ngDialog.closeAll()
                }, function (errorMessage) {
                  openError('An error occurred when updating this datasource: ' + errorMessage.data);
                  stopSpin();
                })
            } else {
              $scope.$parent.InputStore.newTableInput(obj, function () {
                initializeDatasources();
                ngDialog.closeAll();
                stopSpin();
              }, function (errorMessage) {
                openError('An error occurred when saving this datasource: ' + errorMessage.data);
                stopSpin();
              })
            }
          }

          // *** Export Functions ***

          $scope.selectDatasourceCategory = selectDatasourceCategory;
          $scope.saveNewFileInput = saveNewFileInput;
          $scope.saveDatabaseInput = saveDatabaseInput;
          $scope.saveTableInput = saveTableInput;

          // *** Function Calls ***

          loadAvailableFiles();
          loadAvailableDatabases();

        }]
      })
    }

    // ***
    // Confirmation
    // ***

    /**
     * Opens a confirm dialog.
     */
    function openConfirm() {
      ngDialog.openConfirm({
        /*jshint multistr: true */
        template: '\
                <h3>Confirm</h3>\
                <p>{{$parent.confirmDescription}}</p>\
                <p>{{$parent.confirmText}}</p>\
                <div class="ngdialog-buttons">\
                    <button type="button" class="ngdialog-button ngdialog-button-secondary" ng-click="closeThisDialog(0)">No</button>\
                    <button type="button" class="ngdialog-button ngdialog-button-warning" ng-click="$parent.confirmDialog(1)">Yes</button>\
                </div>',
        plain: true,
        scope: $scope
      })
    }

    /**
     * Opens a dialog to confirm that the given item should be deleted or not.
     * @param item the item, either algorithm, file input, database connection or table input
     */
    function confirmDelete(item) {
      var objectToDelete;
      switch (item.type) {
        case 'fileInput':
          objectToDelete = 'file input';
          break;
        case 'databaseConnection':
          objectToDelete = 'database connection';
          break;
        case 'tableInput':
          objectToDelete = 'table input';
          break;
        default:
          objectToDelete = 'algorithm';
      }

      $scope.confirmText = 'Are you sure you want to delete the ' + objectToDelete + '?';
      $scope.confirmDescription = 'Deleting this ' + objectToDelete + ' results also in deleting all executions of this ' + objectToDelete + '. ' +
      'However, the result files remain on disk.';

      $scope.confirmItem = item;
      $scope.confirmFunction = function () {
        switch ($scope.confirmItem.type) {
          case 'fileInput':
            startSpin();
            Delete.file({id: $scope.confirmItem.id}, function () {
              stopSpin();
              initializeDatasources();
              ngDialog.closeAll()
            });
            break;
          case 'databaseConnection':
            startSpin();
            Delete.database({id: $scope.confirmItem.id}, function () {
              stopSpin();
              initializeDatasources();
              ngDialog.closeAll()
            });
            break;
          case 'tableInput':
            startSpin();
            Delete.table({id: $scope.confirmItem.id}, function () {
              stopSpin();
              initializeDatasources();
              ngDialog.closeAll()
            });
            break;
          default:
            startSpin();
            Delete.algorithm({id: $scope.confirmItem.id}, function () {
              stopSpin();
              resetAlgorithm();
              ngDialog.closeAll()
            });
            break
        }
      };
      openConfirm()
    }

    function confirmDialog() {
      $scope.confirmFunction()
    }

    // ***
    // Actions
    // ***

    /**
     * Activates a data source, which was selected.
     * If the number of data sources is equal to the number required by the algorithm, all other not active data
     * sources are hidden.
     * @param datasource the data source the user clicked on
     */
    function toggleDatasource(datasource) {
      var index = activeDataSources[datasource.type].indexOf(datasource.id);
      datasource.active = datasource.active || false;
      datasource.active = !datasource.active;

      if (index === -1) {
        activeDataSources[datasource.type].push(datasource.id)
      } else {
        activeDataSources[datasource.type].splice(index, 1);
        enableAllInputs()
      }

      var type = '';
      if (datasource.type === 'fileInput') {
        type = 'File Input'
      } else if (datasource.type === 'databaseConnection') {
        type = 'Database Connection'
      } else if (datasource.type === 'tableInput') {
        type = 'Table Inputs'
      }

      if ($scope.maxNumberOfSetting[type] !== -1 &&
        $scope.maxNumberOfSetting[type] <= activeDataSources[datasource.type].length) {
        $scope.datasources.forEach(function (ds) {
          if (ds.name.indexOf(type) > -1) {
            ds.datasource.forEach(function (element) {
              if (element.active === undefined || element.active === false) {
                element.disabled = true
              }
            })
          }
        })
      } else {
        enableAllInputs()
      }
    }


    /**
     * Executes an algorithm.
     * Start a timer for the algorithm execution.
     * @param caching the caching parameter, which was selected
     * @param memory the amount of memory
     */
    function executeAlgorithm(caching, memory) {
      var algorithm = $scope.activeAlgorithm;
      var params;
      try {
        params = readParamsIntoBackendFormat(currentParameter)
      } catch (e) {
        openError(e.message);
        return;
      }

      var date = new Date();
      var executionIdentifierDate = date.getFullYear() + '-' +
        twoDigits(date.getMonth() + 1) + '-' +
        twoDigits(date.getDate()) + 'T' +
        twoDigits(date.getHours()) +
        twoDigits(date.getMinutes()) +
        twoDigits(date.getSeconds());
      var payload = {
        'algorithmId': algorithm.id,
        'executionIdentifier': algorithm.fileName + executionIdentifierDate,
        'requirements': params,
        'cacheResults': (caching === 'cache'),
        'writeResults': (caching === 'disk'),
        'countResults': (caching === 'count'),
        'memory': memory || ''
      };
      $scope.payload = payload;
      $scope.canceled = false;
      $scope.cancelFunction = function () {
        $scope.canceled = true
      };
      ngDialog.openConfirm({
        /*jshint multistr: true */
        template: '\
                <h3>Execution running</h3>\
                <timer interval="1000">Elapsed time: {{days}} days, {{hours}} hour{{hoursS}}, {{minutes}} minute{{minutesS}}, {{seconds}} second{{secondsS}}.</timer>\
                <div class="ngdialog-buttons">\
                    <button type="button" class="ngdialog-button ngdialog-button-warning" ng-click="cancelExecution()">Cancel Execution</button>\
                </div>',
        plain: true,
        scope: $scope,
        showClose: false,
        closeByEscape: false,
        closeByDocument: false,
        controller: ['$scope', function ($scope) {
          $scope.cancelExecution = function () {
            $scope.$parent.cancelFunction();
            var params = {identifier: $scope.$parent.payload.executionIdentifier};
            $scope.$parent.StopExecution.stop(params, function () {
              ngDialog.closeAll()
            })
          }
        }]
      });
      AlgorithmExecution.run({}, payload, function (result) {
        var typeStr = '&ind=' + result.algorithm.ind + '&fd=' + result.algorithm.fd + '&ucc=' + result.algorithm.ucc +
          '&cucc=' + result.algorithm.cucc + '&od=' + result.algorithm.od + '&mvd=' + result.algorithm.mvd +
          '&basicStat=' + result.algorithm.basicStat;
        ngDialog.closeAll();
        if (!$scope.canceled) {
          if (caching === 'cache' || caching === 'disk') {
            $location.url('/result/' + result.id + '?cached=true' + typeStr);
          } else {
            $location.url('/result/' + result.id + '?count=true' + typeStr);
          }
        }
      }, function (errorMessage) {
        ngDialog.closeAll();
        openError('The algorithm execution was not successful: ' + errorMessage.data);
      })
    }

    /**
     * Highlights the given algorithm in the algorithm list
     * @param algorithm the algorithm
     */
    function highlightAlgorithm(algorithm) {
      if ($scope.activeAlgorithm) {
        $scope.activeAlgorithm.active = false
      }
      algorithm.active = true;
      $scope.activeAlgorithm = algorithm
    }

    /**
     * Updates the data source list according to the data sources the algorithm needs.
     * @param algorithm the algorithm
     */
    function updateAvailableDatasources(algorithm) {
      $scope.datasources.forEach(function (datasource) {
        if (datasource.name.indexOf('Table Inputs') > -1) {
          datasource.possible = algorithm.tableInput || algorithm.relationalInput
        } else if (datasource.name.indexOf('Database Connection') > -1) {
          datasource.possible = algorithm.databaseConnection
        } else if (datasource.name.indexOf('File Input') > -1) {
          datasource.possible = algorithm.fileInput || algorithm.relationalInput
        }
      })
    }

    /**
     * An algorithm was selected. Load the parameters and show them to the user.
     * @param algorithm the algorithm
     */
    function activateAlgorithm(algorithm) {
      highlightAlgorithm(algorithm);
      updateAvailableDatasources(algorithm);
      enableAllInputs();
      unselectAllInputs();
      resetParameter();
      initializeForm();
      updateParameter(algorithm);
    }

    // ***
    // Parameter Update
    // ***

    /**
     * Reset the parameter form.
     */
    function resetParameter() {
      $scope.model = {};
      $scope.form = [];
      $scope.schema = {
        type: 'object',
        properties: {}
      };
      $scope.algorithmHasCustomProperties = false
    }

    /**
     * Initialize the parameter form
     */
    function initializeForm() {
      $scope.form = [
        '*',
        {
          'type': 'actions',
          'items': []
        }
      ]
    }

    /**
     * Update the parameter of the given algorithm.
     * @param algorithm the algorithm
     */
    function updateParameter(algorithm) {
      currentParameter = [];
      $scope.maxNumberOfSetting = {};
      activeDataSources = {
        'fileInput': [],
        'tableInput': [],
        'databaseConnection': []
      };
      $scope.schema.required = [];

      Parameter.get({algorithm: algorithm.fileName}, function (parameter) {
        // sort parameter, so that the same type of parameter is listed
        // next to each other in the form
        parameter.sort(function (a, b) {
          return a.type.localeCompare(b.type) * -1;
        });
        parameter.forEach(function (param) {
          currentParameter.push(param);
          switch (param.type) {
            case 'ConfigurationRequirementRelationalInput':
              configureParamInputs(param, 'File Input');
              configureParamInputs(param, 'Table Inputs');
              break;
            case 'ConfigurationRequirementFileInput':
              configureParamInputs(param, 'File Input');
              break;
            case 'ConfigurationRequirementTableInput':
              configureParamInputs(param, 'Table Inputs');
              break;
            case 'ConfigurationRequirementDatabaseConnection':
              configureParamInputs(param, 'Database Connection');
              break;
            case 'ConfigurationRequirementInteger':
              addParamToList(param, 'number', false, false);
              break;
            case 'ConfigurationRequirementListBox':
              addParamToList(param, 'string', true, false);
              break;
            case 'ConfigurationRequirementCheckBox':
              addParamToList(param, 'array', false, true);
              break;
            case 'ConfigurationRequirementString':
              addParamToList(param, 'string', false, false);
              break;
            case 'ConfigurationRequirementBoolean':
              addParamToList(param, 'boolean', false, false);
              break;
            default:
              $scope.console.error('Parameter type ' + param.type + ' not supported yet');
              break
          }
        })
      })
    }

    // ***
    // Editing
    // ***

    /**
     * Opens a dialog for editing the given data source.
     * @param datasource the data source
     */
    function editDatasource(datasource) {
      $scope.edit = true;
      $scope.saveUpdateButton = 'Update';
      switch (datasource.type) {
        case 'fileInput':
          $scope.editFileInput = datasource;
          break;
        case 'databaseConnection':
          $scope.editDatabaseInput = datasource;
          break;
        case 'tableInput':
          $scope.editTableInput = datasource;
          break;
      }
      openNewDatasource()
    }

    /**
     * The editing of a data source is done.
     * Reset the editing parameter.
     */
    function doneEditingDatasources() {
      $scope.editFileInput = null;
      $scope.editDatabaseInput = null;
      $scope.editTableInput = null;
      $scope.edit = false;
      $scope.saveUpdateButton = 'Save';
    }

    /**
     * The editing of an algorithm is done.
     * Reset the editing parameter.
     */
    function doneEditingAlgorithm() {
      $scope.AlgorithmToEdit = null;
      $scope.edit = false;
      $scope.saveUpdateButton = 'Save';
    }

    /**
     * Opens the algorithm dialog for editing the given algorithm.
     * @param algorithm the algorithm
     */
    function editAlgorithm(algorithm) {
      $scope.edit = true;
      $scope.saveUpdateButton = 'Update';
      $scope.AlgorithmToEdit = algorithm;
      openNewAlgorithm();
    }

    // ***
    // Parameter Configuration
    // ***

    /**
     * Shows the user the number of data sources he has to select.
     * @param param the parameter
     * @param input the input type
     */
    function configureParamInputs(param, input) {
      var index = -1;
      $.grep($scope.datasources, function (element, i) {
        if (element !== undefined && element.name !== undefined && element.name.indexOf(input) > -1) {
          index = i;
          return true
        } else {
          return false
        }
      });
      if (param.numberOfSettings === -1) {
        $scope.datasources[index].name =
          input + ' (choose an arbitrary number)'
      } else if (param.minNumberOfSettings === param.maxNumberOfSettings) {
        $scope.datasources[index].name =
          input + ' (choose ' + param.minNumberOfSettings + ')'
      } else {
        $scope.datasources[index].name =
          input + ' (min: ' + param.minNumberOfSettings + ' | max: ' + param.maxNumberOfSettings + ')'
      }
      $scope.maxNumberOfSetting[input] = $scope.maxNumberOfSetting[input] || 0;
      $scope.maxNumberOfSetting[input] += param.maxNumberOfSettings;
    }

    /**
     * Adds the given parameter to parameter form.
     * @param param the parameter
     * @param type the type of the input
     * @param dropdown true, if the parameter requires a dropdown, false otherwise
     */
    function addParamToList(param, type, useEnum, useForm) {
      $scope.algorithmHasCustomProperties = true;
      var identifier;

      if (param.numberOfSettings > 1) {
        for (var i = 1; i <= param.numberOfSettings; i++) {
          identifier = param.identifier + ' (' + i + ')';
          $scope.schema.properties[identifier] = {
            'title': identifier,
            'type': type
          };

          if(useForm) {
            $scope.form.key = identifier;
            $scope.form.type = "checkboxes";
            $scope.form.titleMap = [];
            param.values.forEach(function (v) {
              $scope.form.titleMap.push({"value": v, "name": v});
            })
            $scope.schema.properties[identifier].items = {};
            $scope.schema.properties[identifier].items.type="string";
            $scope.schema.properties[identifier].items.enum = [];
            param.values.forEach(function (v) {
              $scope.schema.properties[identifier].items.enum.push(v)
            })
          }
          if (useEnum) {
              $scope.schema.properties[identifier].enum = [];
              param.values.forEach(function (v) {
                $scope.schema.properties[identifier].enum.push(v)
              })
          }
          if (param.required) {
            $scope.schema.required.push(identifier)
          }
          if (param.defaultValues !== null && param.defaultValues !== undefined) {
            $scope.schema.properties[identifier].default = param.defaultValues[i]
          }
        }
      } else {
        identifier = param.identifier;
        $scope.schema.properties[identifier] = {
          'title': identifier,
          'type': type
        };
        if(useForm) {
          $scope.form.key = identifier;
          $scope.form.type = "checkboxes";
          $scope.form.titleMap = [];
          param.values.forEach(function (v) {
            $scope.form.titleMap.push({"value": v, "name": v});
          })
          $scope.schema.properties[identifier].items = {};
          $scope.schema.properties[identifier].items.type="string";
          $scope.schema.properties[identifier].items.enum = [];
          param.values.forEach(function (v) {
            $scope.schema.properties[identifier].items.enum.push(v)
          })
        }
        if (useEnum) {
            $scope.schema.properties[identifier].enum = [];
            param.values.forEach(function (v) {
              $scope.schema.properties[identifier].enum.push(v)
            })
        }

        if (param.required) {
          $scope.schema.required.push(identifier)
        }
        if (param.defaultValues !== null && param.defaultValues !== undefined) {
          $scope.schema.properties[identifier].default = param.defaultValues[0]
        }
      }
    }

    // ***
    // Helper
    // ***

    /**
     * Stars the spinner
     */
    function startSpin() {
      usSpinnerService.spin('spinner-1');
    }

    /**
     * Stops the spinner
     */
    function stopSpin() {
      usSpinnerService.stop('spinner-1');
    }

    /**
     * Formats the given number. The number should contain two digits.
     * @param number the number
     * @returns {string} a string containing two digits
     */
    function twoDigits(number) {
      return (number < 10 ? '0' + number : '' + number)
    }

    /**
     * Remove duplicates from the given array.
     * @param a the array
     */
    function removeDuplicates(a) {
      var ids = a.map(function(f) {return f.id});
      return a.filter(function(item, pos) {
        return ids.indexOf(item.id) == pos;
      });
    }

    /**
     * Reset the selected algorithm.
     */
    function resetAlgorithm() {
      initializeAlgorithmList();
      initializeDatasources();
      resetParameter();
      $scope.activeAlgorithm = undefined
    }

    /**
     * Reset the selected data sources.
     */
    function resetDataSources() {
      enableAllInputs();
      unselectAllInputs();
      resetParameter();
    }

    /**
     * Activates all inputs.
     */
    function unselectAllInputs() {
      [
        'File Input',
        'Database Connection',
        'Table Inputs'
      ].forEach(function () {
          $scope.datasources.forEach(function (ds) {
            ds.datasource.forEach(function (element) {
              element.active = false
            })
          })
        })
    }

    /**
     * Enables all inputs.
     */
    function enableAllInputs() {
      [
        'File Input',
        'Database Connection',
        'Table Inputs'
      ].forEach(function () {
          $scope.datasources.forEach(function (ds) {
            ds.datasource.forEach(function (element) {
              element.disabled = false
            })
          })
        })
    }

    /**
     * Reads the setting of the given parameter.
     * @param param the configuration parameter
     * @param typeValue the type of the parameter
     * @returns {*}
     */
    function readSetting(param, typeValue) {
      var settingValue;
      var j;

      if (param.numberOfSettings > 1) {
        for (j = param.numberOfSettings; j > 0; j--) {
          settingValue = $scope.model[param.identifier + ' (' + j + ')'];
          // only set the value if it is set
          if (settingValue !== undefined) {
            param.settings.push({
              'type': typeValue,
              'value': settingValue
            })
          }
        }
      } else {
        settingValue = $scope.model[param.identifier];
        if (settingValue !== undefined) {
          param.settings.push({
            'type': typeValue,
            'value': settingValue
          })
        }
      }

      return param
    }

    /**
     * Reads the parameter the user typed in.
     * The parameters have to be converted into a specific format, which the backend understands.
     * @param params the parameters
     * @returns {*}
     */
    function readParamsIntoBackendFormat(params) {
      var i, j;
      var checked, param, item, paramNumberOfSettings;

      for (i = 0; i < params.length; i++) {
        params[i].settings = [];

        //needed because same fields vary in different places in backend - workaround!
        if (params[i].fixNumberOfSettings !== undefined) {
          delete params[i].fixNumberOfSettings
        }

        paramNumberOfSettings = (params[i].numberOfSettings !== -1) ? params[i].numberOfSettings : 1000;

        switch (params[i].type) {

          case 'ConfigurationRequirementInteger':
            params[i] = readSetting(params[i], 'ConfigurationSettingInteger');
            break;

          case 'ConfigurationRequirementString':
            params[i] = readSetting(params[i], 'ConfigurationSettingString');
            break;

          case 'ConfigurationRequirementBoolean':
            params[i] = readSetting(params[i], 'ConfigurationSettingBoolean');
            break;

          case 'ConfigurationRequirementListBox':
            params[i] = readSetting(params[i], 'ConfigurationSettingListBox');
            break;

          case 'ConfigurationRequirementCheckBox':
            params[i] = readSetting(params[i], 'ConfigurationSettingCheckBox');
            break;

          case 'ConfigurationRequirementTableInput':
            checked = activeDataSources.tableInput.slice(0);
            for (j = 1; j <= paramNumberOfSettings && checked.length > 0; j++) {
              item = dataSources.tableInput['' + checked.pop()];
              //needed because same fields are named different in different places in
              // backend - workaround!
              param = {
                'table': item.tableName,
                'databaseConnection': {
                  'dbUrl': item.databaseConnection.url,
                  'username': item.databaseConnection.username,
                  'password': item.databaseConnection.password,
                  'system': item.databaseConnection.system,
                  'type': 'ConfigurationSettingDatabaseConnection',
                  'id': item.databaseConnection.id
                },
                'type': 'ConfigurationSettingTableInput',
                'id': item.id
              };
              params[i].settings.push(param)
            }
            break;

          case 'ConfigurationRequirementDatabaseConnection':
            checked = activeDataSources.databaseConnection.slice(0);
            for (j = 1; j <= paramNumberOfSettings && checked.length > 0; j++) {
              item = dataSources.databaseConnection['' + checked.pop()];
              //needed because same fields are named different in different places in
              // backend - workaround!
              param = {
                'dbUrl': item.url,
                'username': item.username,
                'password': item.password,
                'system': item.system,
                'type': 'ConfigurationSettingDatabaseConnection',
                'id': item.id
              };
              params[i].settings.push(param)
            }
            break;

          case 'ConfigurationRequirementFileInput':
            checked = activeDataSources.fileInput.slice(0);
            for (j = 1; j <= paramNumberOfSettings && checked.length > 0; j++) {
              item = dataSources.fileInput['' + checked.pop()];
              // needed because same fields are named different in different places in
              // backend - workaround!
              param = {
                'fileName': item.fileName,
                'advanced': false,
                'separatorChar': item.separator,
                'quoteChar': item.quoteChar,
                'escapeChar': item.escapeChar,
                'strictQuotes': item.strictQuotes,
                'ignoreLeadingWhiteSpace': item.ignoreLeadingWhiteSpace,
                'skipLines': item.skipLines,
                'header': item.hasHeader,
                'skipDifferingLines': item.skipDifferingLines,
                'nullValue': item.nullValue,
                'type': 'ConfigurationSettingFileInput',
                'id': item.id
              };
              params[i].settings.push(param)
            }
            break;

          case 'ConfigurationRequirementRelationalInput':
            // add table inputs
            checked = activeDataSources.tableInput.slice(0);
            for (j = 1; j <= paramNumberOfSettings && checked.length > 0; j++) {
              item = dataSources.tableInput['' + checked.pop()];
              //needed because same fields are named different in different places in
              // backend - workaround!
              param = {
                'table': item.tableName,
                'databaseConnection': {
                  'dbUrl': item.databaseConnection.url,
                  'username': item.databaseConnection.username,
                  'password': item.databaseConnection.password,
                  'system': item.databaseConnection.system,
                  'type': 'ConfigurationSettingDatabaseConnection',
                  'id': item.databaseConnection.id
                },
                'type': 'ConfigurationSettingTableInput',
                'id': item.id
              };
              params[i].settings.push(param)
            }
            // add file inputs
            checked = activeDataSources.fileInput.slice(0);
            for (j = 1; j <= paramNumberOfSettings && checked.length > 0; j++) {
              item = dataSources.fileInput['' + checked.pop()];
              //needed because same fields are named different in different places in
              // backend - workaround!
              param = {
                'fileName': item.fileName,
                'advanced': false,
                'separatorChar': item.separator,
                'quoteChar': item.quoteChar,
                'escapeChar': item.escapeChar,
                'strictQuotes': item.strictQuotes,
                'ignoreLeadingWhiteSpace': item.ignoreLeadingWhiteSpace,
                'skipLines': item.skipLines,
                'header': item.hasHeader,
                'skipDifferingLines': item.skipDifferingLines,
                'nullValue': item.nullValue,
                'type': 'ConfigurationSettingFileInput',
                'id': item.id
              };
              params[i].settings.push(param)
            }
            break;

          default:
            $scope.console.error('Parameter Type "' + params[i].type + '" not not supported yet for execution!');
            break;
        }

        // check if required number of parameters are set
        var numberOfSettings = params[i].settings.length;
        if (params[i].required && params[i].numberOfSettings !== -1 &&
          numberOfSettings !== params[i].numberOfSettings &&
          (numberOfSettings < params[i].minNumberOfSettings ||
          numberOfSettings > params[i].maxNumberOfSettings)
        ) {
          throw new WrongParameterError('Wrong value or number for parameter "' + params[i].identifier + '"!')
        }
      }

      return params
    }

    /**
     * Error: the number of parameters is selected.
     * @param message the message of the error
     */
    function WrongParameterError(message) {
      this.name = 'WrongParameterError';
      this.message = (message || '');
    }

    WrongParameterError.prototype = Error.prototype;

    /**
     * Opens a dialog to show the given error message.
     * @param message the error message
     */
    function openError(message) {
      $scope.errorMessage = message;
      ngDialog.open({
        /*jshint multistr: true */
        template: '\
                <h3 style="color: #F44336">ERROR</h3>\
                <p>{{errorMessage}}</p>\
                <div class="ngdialog-buttons">\
                    <button type="button" class="ngdialog-button ngdialog-button-secondary" ng-click="closeThisDialog(0)">Ok</button>\
                </div>',
        plain: true,
        scope: $scope
      })
    }

    /**
     * Opens a dialog for showing the file input help when adding/updating a file input.
     */
    function openFileInputHelp() {
      ngDialog.open({
        /*jshint multistr: true */
        template: '\
                <h3 style="color: rgb(63, 81, 181);">File Input Help</h3><br>\
                <p>If you want to add new files to the available files, add your files to the folder <i>"/WEB-INF/classes/inputData/"</i>.\
                CSV and TSV files are accepted by Metanome. The files need to be UTF-8 or ASCII encoded.</p><br/>\
                <p><b>Additional settings</b>:<br>\
                Do not escape any characters, Metanome is taking care of that.\
                For example, if you want a backslash as escape character, just write a single "\\". \
                For tab separator type in "\\t". \
                For the null string you can either type in "\\0" or just leave the field empty. </p><br/>\
                <p>Make sure, that you set the correct settings, so that your file input can be read. \
                If a line could not be read properly, <i>null</i> is returned.\
                Internally, <i>au.com.bytecode.opencsv.CSVReader</i> is used to read the input file.\
                </p>\
                <div class="ngdialog-buttons">\
                    <button type="button" class="ngdialog-button ngdialog-button-secondary" ng-click="closeThisDialog(0)">Ok</button>\
                </div>',
        plain: true,
        scope: $scope
      })
    }

    /**
     * Opens dialog for showing the algorithm help when adding/updating an algorithm.
     */
    function openAlgorithmHelp() {
      ngDialog.open({
        /*jshint multistr: true */
        template: '\
                <h3 style="color: rgb(63, 81, 181);">Algorithm Help</h3><br>\
                <p>If you want to add new algorithm files to the available jar-files, add your jar-files to the folder <i>"/WEB-INF/classes/algorithms/"</i>.</p><br/>\
                <div class="ngdialog-buttons">\
                    <button type="button" class="ngdialog-button ngdialog-button-secondary" ng-click="closeThisDialog(0)">Ok</button>\
                </div>',
        plain: true,
        scope: $scope
      })
    }


    // ** EXPORT FUNCTIONS **
    // **********************

    //Exported functions
    $scope.openAlgorithmSettings = openNewAlgorithm;
    $scope.openDatasourceSettings = openNewDatasource;
    $scope.executeAlgorithm = executeAlgorithm;
    $scope.toggleDatasource = toggleDatasource;
    $scope.activateAlgorithm = activateAlgorithm;
    $scope.confirmDelete = confirmDelete;
    $scope.confirmDialog = confirmDialog;
    $scope.editAlgorithm = editAlgorithm;
    $scope.editDatasource = editDatasource;
    $scope.resetAlgorithm = resetAlgorithm;
    $scope.resetDataSources = resetDataSources;
    $scope.openFileInputHelp = openFileInputHelp;
    $scope.openAlgorithmHelp = openAlgorithmHelp;

    //Exports for dialogs
    $scope.InputStore = InputStore;
    $scope.AvailableAlgorithmFiles = AvailableAlgorithmFiles;
    $scope.AvailableInputFiles = AvailableInputFiles;
    $scope.StopExecution = StopExecution;
    $scope.doneEditingDatasources = doneEditingDatasources;


    // ** FUNCTION CALLS **
    // ********************

    // Execute initialization
    initializeAlgorithmList();
    initializeDatasources();
    resetParameter();


  });
