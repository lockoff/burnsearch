'use strict';

angular.module('burnsearchApp')
    .controller('EventController', function($scope, event) {
        $scope.event = event;
        if (event) {
            $scope.showInfoTable = event.url || event.hostingCamp || event.locatedAtArt;
        }
    });
