'use strict';

angular.module('burnsearchApp')
    .controller('MainController', function ($state, $scope, Principal) {
        Principal.identity().then(function(account) {
            $scope.account = account;
            $scope.isAuthenticated = Principal.isAuthenticated;
        });
    });
