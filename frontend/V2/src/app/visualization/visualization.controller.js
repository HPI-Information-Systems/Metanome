'use strict';

var app = angular.module('v2')

.config(function config( $stateProvider ) {
  $stateProvider
    .state('visualization', {
      url: '/visualization/:resultId',
      views: {
        'main@': {
            controller: 'VisualizationCtrl',
            templateUrl: 'app/visualization/visualization.html'
         }
      }
    })
})

app.controller('VisualizationCtrl', function ($scope, $stateParams) {
  $scope.id = $stateParams.resultId

})
