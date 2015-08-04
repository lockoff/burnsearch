'use strict';

angular.module('burnsearchApp')
    .controller('PlanPrintController', function($rootScope, $scope, $window, $timeout, campsPlan, eventsPlan) {
        $scope.campsPlan = campsPlan;
        $scope.eventsPlan = eventsPlan;

        $timeout(function() {
            // Calling print in timeout like this has the side effect of ensuring the DOM has
            // rendered the content to print first.
            $window.print();
            $rootScope.back();
        });
    });
