'use strict';

angular.module('burnsearchApp')
    .controller('CampSearchController', function($scope, $state, $stateParams, $window, campsPage) {
        $scope.pageChanged = function () {
            $state.go('camps', {q: $stateParams.q, campsPageNum: $scope.currentPage - 1});
            $window.scrollTo(0,0);
        };
        $scope.currentPage = campsPage.pageNumber + 1;
        $scope.totalCamps = campsPage.totalCamps;
        $scope.campResults = campsPage.camps;
        $scope.$parent.searchEntityTab = 'Camps';
    });

