'use strict';

angular.module('burnsearchApp')
    .controller('NavbarController', function ($scope, $location, $state, Auth, Principal, PlanService) {
        $scope.isAuthenticated = Principal.isAuthenticated;
        $scope.$state = $state;
        $scope.query = '';

        $scope.$on('$stateChangeSuccess', function(event, toState, toParams, fromState, fromParams) {
            $scope.query = toParams.q;
        });

        $scope.logout = function () {
            Auth.logout();
            PlanService.clearPlans();
            $state.go('home');
        };
    });
