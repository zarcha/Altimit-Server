'use strict';

var app = angular.module('altimitCPanel', ['ngRoute']);

angular.module('altimitCPanel').config(['$routeProvider',
  function ($routeProvider) {
    $routeProvider.
    when('/', {
      templateUrl: 'views/main.html',
      controller: 'MainCtrl',
      controllerAs: "Ctrl"
    }).
    when('/users', {
      templateUrl: 'views/users.html',
      controller: 'UserCtrl',
      controllerAs: "Ctrl"
    }).
    when('/rooms', {
      templateUrl: 'views/rooms.html',
      controller: 'RoomCtrl',
      controllerAs: "Ctrl"
    }).
      otherwise({
        redirectTo: '/'
    });
  }]);
