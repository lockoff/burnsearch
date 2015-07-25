'use strict';

angular.module('burnsearchApp')
    .controller('EventSearchController', function($scope, $http, $stateParams, $window) {
        $scope.pageChanged = function () {
            if ($scope.$parent.currentEventsPage == $scope.$parent.lastEventsPage) return;
            console.log("getting a page");
            $http.get('/api/events/search/description?q=' + $stateParams.q + "&page=" + $scope.$parent.currentEventsPage).then(
                function(response) {
                    var eventResults = response.data;
                    $scope.$parent.eventResults = eventResults.content;
                    $scope.$parent.currentEventsPage = eventResults.currentPage;
                    $scope.$parent.totalEvents = eventResults.totalItems;
                    $scope.$parent.lastEventsPage = $scope.$parent.currentEventsPage;
                    $window.scrollTo(0,0);
                }
            )
        };
        $scope.pageChanged();
    });

