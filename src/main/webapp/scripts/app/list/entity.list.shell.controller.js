'use strict';

angular.module('burnsearchApp')
    .controller('EntityListShellController', function($scope, $state) {

        $scope.transitionEvents = function() {
            $state.transitionTo('events', $state.params);
        };

        $scope.transitionCamps = function() {
            $state.transitionTo('camps', $state.params);
        };

    });

