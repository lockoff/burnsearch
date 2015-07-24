'use strict';

angular.module('burnsearchApp')
    .controller('SearchController', function($scope, eventResults, campResults) {
        $scope.searchEntityTab = 'Events';
        $scope.eventResults = eventResults;
        $scope.campResults = campResults;
    });

