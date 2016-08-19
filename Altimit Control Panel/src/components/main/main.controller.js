angular.module('altimitCPanel').controller('MainCtrl', ['$scope', '$http', '$timeout', function ($scope, $http, $timeout) {
  var me = this;

  //Prevent from calling after server is off line.
  me.makeCalls = true;

  me.callServer = function () {
    //Room count request
    $http.get('http://localhost:4567/roomCount')
      .then(function (response) {
        me.roomCount = response.data;
      }, function () {
        me.roomCount = "N/A";
      });

    //User count request
    $http.get('http://localhost:4567/userCount')
      .then(function (response) {
        me.userCount = response.data;
      }, function () {
        me.userCount = "N/A";
      });

    //Server status
    $http.get('http://localhost:4567/serverStatus')
      .then(function (response) {
        me.serverStatus = "Online";
      }, function () {
        me.serverStatus = "Offline";
        me.makeCalls = false;
      });
  };

  me.callServer();

  $timeout(function() {
    if (me.makeCalls){
      me.callServer();
    }
  }, 15000);

}]);
