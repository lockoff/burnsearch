'use strict';

angular.module('burnsearchApp')
    .controller('SearchController', function($scope, $state, $stateParams) {
        $scope.searchEntityTab = 'Events';

        $scope.lastEventsPage = 0;
        $scope.currentEventsPage = 1;
        $scope.totalEvents = undefined;

        $scope.lastCampsPage = 0;
        $scope.currentCampsPage = 1;
        $scope.totalCamps = undefined;

        $scope.transitionEvents = function() {
            $state.transitionTo('events', $state.params);
        };

        $scope.transitionCamps = function() {
            if (!$state.params.campsPageNum) {
                $state.params.campsPageNum = 0;
            }
            $state.transitionTo('camps', $state.params);
        };

    });

