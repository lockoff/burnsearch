'use strict';

angular.module('burnsearchApp')
    .controller('LogoutController', function (Auth) {
        Auth.logout();
    });
