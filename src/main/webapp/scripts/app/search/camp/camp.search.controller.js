'use strict';

angular.module('burnsearchApp')
    .controller('CampSearchController', function($scope, campResults) {
        console.log("IN CAMPS");
        $scope.campResults = campResults;
    });

