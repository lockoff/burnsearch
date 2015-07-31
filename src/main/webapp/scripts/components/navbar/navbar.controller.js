'use strict';

angular.module('burnsearchApp')
    .controller('NavbarController', function ($scope, $location, $state, Auth, Principal, PlanService) {
        $scope.isAuthenticated = Principal.isAuthenticated;
        $scope.$state = $state;
        $scope.query = '';

        $scope.logout = function () {
            Auth.logout();
            PlanService.clearPlans();
            $state.go('home');
        };
    });
