'use strict';

angular.module('Metanome', [
  'ngAnimate',
  'ngCookies',
  'ngResource',
  'ui.router',
  'ngMaterial',
  'ngDialog',
  'schemaForm',
  'angularSpinner',
  'md.data.table',
  'timer'
])
  .config(function ($stateProvider, $urlRouterProvider) {
    $urlRouterProvider.otherwise('/new');
  })
;
