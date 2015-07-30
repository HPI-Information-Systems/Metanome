'use strict';

angular.module('v2.home', [
  'ui.router'
])

  .config(function config( $stateProvider ) {
    $stateProvider
      .state('home', {
        url: '/',
        views: {
          'main@': {
             controller: 'MainCtrl',
             templateUrl: 'app/main/main.html'
           },
           'new@home': {
              controller: 'NewCtrl',
              templateUrl: 'app/new/new.html'
           },
           'history@home': {
              controller: 'HistoryCtrl',
              templateUrl: 'app/history/history.html'
           },
           'result@home': {
              controller: 'ResultCtrl',
              templateUrl: 'app/result/result.html'
           }
        }
      })
  })

  .controller('MainCtrl', function ($scope, $log, $rootScope) {
    var tabs = [
      { 
        title: 'New', 
        view: 'new'
      },
      { 
        title: 'History', 
        view: 'history'
      }
   ],
      selected = null,
      previous = null
    $scope.tabs = tabs
    $scope.selectedIndex = 0
    $scope.$watch('selectedIndex', function(current, old){
      previous = selected
      selected = $scope.tabs[current]
      if(old && (old !== current)){
        $log.debug('Hide ' + previous.title + '!')
      }
      if(current){
        $log.debug('Show ' + selected.title + '!')
      }
      $rootScope.currentTab = selected.title
    })
    $rootScope.$on('changeTab', function(event, data) { 
      //Go to last tab
      if(data = -1) {
        $scope.selectedIndex = $scope.tabs.length - 1
      } else {
        $scope.selectedIndex = data
      }
    });
    $rootScope.addResultsTab = function(){
      if($scope.tabs.length < 3) {
        $scope.addTab('Result', 'result')
      }
    }
    $scope.addTab = function (title, view) {
      view = view || title + ' Content View'
      tabs.push({ title: title, view: view, disabled: false})
    }
    $scope.removeTab = function (tab) {
      var index = tabs.indexOf(tab)
      tabs.splice(index, 1)
    };
});
