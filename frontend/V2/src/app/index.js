'use strict';

angular.module('v2', [
  'ngAnimate', 
  'ngCookies', 
  'ngResource', 
  'ui.router', 
  'ngMaterial',
  'ngDialog',
  'schemaForm',
  'angularSpinner',
  'md.data.table'
])
  .config(function ($stateProvider, $urlRouterProvider) {
    $urlRouterProvider.otherwise('/new');
  })
;
