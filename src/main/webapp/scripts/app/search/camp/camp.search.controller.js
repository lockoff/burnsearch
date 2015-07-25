'use strict';

angular.module('burnsearchApp')
    .controller('CampSearchController', function($scope, $http, $stateParams, $window) {
        $scope.pageChanged = function () {
            if ($scope.$parent.currentCampsPage == $scope.$parent.lastCampsPage) return;
            console.log("getting a camp page");
            $http.get('/api/camps/search/description?q=' + $stateParams.q + "&page=" + $scope.$parent.currentCampsPage).then(
                function(response) {
                    var campResults = response.data;
                    $scope.$parent.campResults = campResults.content;
                    $scope.$parent.currentCampsPage = campResults.currentPage;
                    $scope.$parent.totalCamps = campResults.totalItems;
                    $scope.$parent.lastCampsPage = $scope.$parent.currentCampsPage;
                    $window.scrollTo(0,0);
                }
            )
        };
        $scope.pageChanged();
    });

