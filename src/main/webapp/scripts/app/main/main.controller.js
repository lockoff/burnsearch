'use strict';

angular.module('burnsearchApp')
    .controller('MainController', function ($state, $scope, Principal) {
        Principal.identity().then(function(account) {
            $scope.account = account;
            $scope.isAuthenticated = Principal.isAuthenticated;
        });
        var states = $state.get();
        for (var i = 0; i < states.length; i++) {
            console.log(states[i]);
        }
    });
