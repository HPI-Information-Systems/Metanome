'use strict';

angular.module('v2', [
  'v2.home',
  'ngAnimate', 
  'ngCookies', 
  'ngResource', 
  'ui.router', 
  'ngMaterial',
  'ngDialog'
])
  .config(function ($stateProvider, $urlRouterProvider) {
    $urlRouterProvider.otherwise('/');
  })
;
