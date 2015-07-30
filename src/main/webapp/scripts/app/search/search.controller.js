'use strict';

angular.module('burnsearchApp')
    .controller('SearchController', function($scope, $state) {
        $scope.searchEntityTab = 'events';

        $scope.transitionEvents = function() {
            $state.transitionTo('events', $state.params);
        };

        $scope.transitionCamps = function() {
            $state.transitionTo('camps', $state.params);
        };

    });

