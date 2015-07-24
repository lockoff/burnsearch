'use strict';

angular.module('burnsearchApp')
    .controller('EventController', function($scope, event) {
        $scope.event = event;
    });
