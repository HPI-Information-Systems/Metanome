'use strict';

angular.module('v2')
  .controller('MainCtrl', function ($scope, $log) {
    var tabs = [
      { title: 'New', content: 'Tabs will become paginated if there isn\'t enough room for them.'},
      { title: 'History', content: 'You can swipe left and right on a mobile device to change tabs.'}
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
