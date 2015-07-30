'use strict';

angular.module('burnsearchApp')
    .controller('EventController', function($scope, event) {
        $scope.entity = event;
        $scope.entityType = 'events';
    });
