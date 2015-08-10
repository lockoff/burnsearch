'use strict';

angular.module('burnsearchApp')
    .controller('NavbarController', function ($scope, $location, $state, Auth, Principal) {
        $scope.isAuthenticated = Principal.isAuthenticated;
        $scope.$state = $state;
        $scope.query = '';

        $scope.$on('$stateChangeSuccess', function(event, toState, toParams, fromState, fromParams) {
            $scope.query = toParams.q;
        });

        $scope.logout = function () {
            Auth.logout();
            $state.go('home');
        };
    });
