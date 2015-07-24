'use strict';

angular.module('burnsearchApp')
    .controller('SearchController', function($scope, $state, $stateParams) {
        $scope.searchEntityTab = 'Events';
        $scope.transitionEvents = function() {
            $state.transitionTo('events', $stateParams);
        };

        $scope.transitionCamps = function() {
            $state.transitionTo('camps', $stateParams);
        };

        $scope.transitionEvents();
    });

