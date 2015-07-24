'use strict';

angular.module('burnsearchApp')
    .controller('CampSearchController', function($scope, campResults) {
        $scope.campResults = campResults;
    });
