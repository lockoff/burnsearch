'use strict';

angular.module('burnsearchApp')
    .controller('EntityListShellController', function($scope, $state, mode) {

        $scope.transitionEvents = function() {
            $state.transitionTo('events' + mode, $state.params);
        };

        $scope.transitionCamps = function() {
            $state.transitionTo('camps' + mode, $state.params);
        };

    });

