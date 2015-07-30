'use strict';

angular.module('burnsearchApp')
    .controller('CampController', function($scope, camp) {
        $scope.entity = camp;
        $scope.entityType = 'camps';
    });
