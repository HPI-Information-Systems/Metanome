'use strict';

angular.module('v2')
  .controller('NewCtrl', function ($scope, $log) {
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


    $scope.category = [
      {
        name: 'Unique Column Combinations',
        algorithms: [
          {
            name: 'example_holistic_algorithm',
            desc: 'Example description',
          },
          {
            name: 'example_relational_input_algorithm',
            desc: 'Example description',
          },
          {
            name: 'example_ucc_algorithm',
            desc: 'Example description'
          }
        ]
      },
      {
        name: 'Conditional Unique Column Combinations',
        algorithms: [
          {
            name: 'example_cucc_algorithm',
            desc: 'Example description',
          }
        ]
      },
      {
        name: 'Functional Dependencies',
        algorithms: [
          {
            name: 'example_cucc_algorithm',
            desc: 'Example description',
          }
        ]
      }
    ]
  });
