'use strict';

angular.module('burnsearchApp')
    .controller('EventSearchController', function($scope, eventResults) {
        $scope.eventResults = eventResults;
    });
