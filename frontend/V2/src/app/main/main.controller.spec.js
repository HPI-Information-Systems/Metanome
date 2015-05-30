'use strict';

describe('controllers', function(){
  var scope;

  beforeEach(module('v2'));

  beforeEach(inject(function($rootScope) {
    scope = $rootScope.$new();
  }));

  it('should have at least 2 tabs', inject(function($controller) {
    expect(scope.tabs).toBeUndefined();

    $controller('MainCtrl', {
      $scope: scope
    });

    expect(angular.isArray(scope.tabs)).toBeTruthy();
    expect(scope.tabs.length > 1).toBeTruthy();
  }));
});
