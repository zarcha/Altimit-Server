angular.module('altimitCPanel').controller('RoomCtrl', ['$scope', '$http', function ($scope, $http) {
  var me = this;


  me.getRoomList = function () {
    //Get user list from server
    $http.get('http://localhost:4567/roomList')
      .then(function (response) {
        console.log(response);
        me.roomList = response.data;
      }, function () {
        console.log(response);
      });
  };

  me.getRoomList();

  me.getLength = function (obj) {
    return Object.keys(obj).length;
  };
}]);
