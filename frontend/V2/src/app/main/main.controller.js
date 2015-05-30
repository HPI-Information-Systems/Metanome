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
           }
        }
      })
  })

  .controller('MainCtrl', function ($scope, $log) {
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
      selected = tabs[current]
      if(old && (old !== current)){
        $log.debug('Hide ' + previous.title + '!')
      }
      if(current){
        $log.debug('Show ' + selected.title + '!')
      }
    })
    $scope.addTab = function (title, view) {
      view = view || title + ' Content View'
      tabs.push({ title: title, content: view, disabled: false})
    }
    $scope.removeTab = function (tab) {
      var index = tabs.indexOf(tab)
      tabs.splice(index, 1)
    };
});
