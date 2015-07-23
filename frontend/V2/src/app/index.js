'use strict';

angular.module('v2', [
  'v2.home',
  'ngAnimate', 
  'ngCookies', 
  'ngResource', 
  'ui.router', 
  'ngMaterial',
  'ngDialog',
  'schemaForm'
])
  .config(function ($stateProvider, $urlRouterProvider) {
    $urlRouterProvider.otherwise('/');
  })
;
