'use strict';

angular.module('burnsearchApp')
    .controller('EventSearchController', function($scope, $state, $stateParams, $window, eventsPage) {
        $scope.pageChanged = function () {
            $state.go('events', {q: $stateParams.q, eventsPageNum: ($scope.currentPage - 1)});
            $window.scrollTo(0,0);
        };
        $scope.currentPage = eventsPage.pageNumber + 1;
        $scope.totalEvents = eventsPage.totalEvents;
        $scope.eventResults = eventsPage.events;
        $scope.$parent.searchEntityTab = 'Events';
    });

