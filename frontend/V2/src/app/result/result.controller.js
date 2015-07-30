'use strict';

var app = angular.module('v2')
 
app.controller('ResultCtrl', function ($scope, $log, Executions, $rootScope, $http) {

 var response = {
  "header": [
    {
      "key": "name",
      "name": "Name"
    },
    {
      "key": "star",
      "name": "Star"
    },
    {
      "key": "sf-location",
      "name": "SF Location"
    }
  ],
  "rows": [
    {
      "name": "Andytown Coffee Roasters",
      "star": "★★★",
      "sf-location": "Outer Sunset"
    },
    {
      "name": "Beanery",
      "star": "★★★",
      "sf-location": "Inner Sunset"
    },
    {
      "name": "Biscoff Coffee Corner",
      "star": "★★★",
      "sf-location": "Fisherman’s Wharf"
    },
    {
      "name": "Blue Bottle",
      "star": "★★★★★",
      "sf-location": "Hayes Valley"
    },
    {
      "name": "Blue Bottle",
      "star": "★★★★★",
      "sf-location": "Embarcadero"
    }
  ],
  "pagination": {
    "count": 5,
    "page": 1,
    "pages": 7,
    "size": 34
  },
  "sort-by": "name",
  "sort-order": "asc"
}

  $scope.getResource = function (params, paramsObj) {
    var urlApi = '?' + params;
    return $http.get(urlApi).then(function (res) {
      return {
        'rows': response.rows,
        'header': response.header,
        'pagination': response.pagination,
        'sortBy': response['sort-by'],
        'sortOrder': response['sort-order']
      }
    });
  }

})
