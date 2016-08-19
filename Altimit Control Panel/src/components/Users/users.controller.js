angular.module('altimitCPanel').controller('UserCtrl', ['$scope', '$http', function ($scope, $http) {
  var me = this;


  me.getUserList = function () {
    //Get user list from server
    $http.get('http://localhost:4567/userList')
      .then(function (response) {
        console.log(response);
        me.userList = response.data;
      }, function () {
        console.log(response);
      });
  };

  me.getUserList();

  me.kickPlayer = function (uuid) {

    console.log(uuid)

    var req = {
      method: 'POST',
      url: 'http://localhost:4567/kickPlayer',
      headers: {
        'Content-Type': undefined
      },
      data: { playerUUID: uuid }
    };

    $http(req)
      .then(function () {
        me.getUserList();
      }), function () {
        console.log("Was not able to kick player");
    };
  }
}]);
