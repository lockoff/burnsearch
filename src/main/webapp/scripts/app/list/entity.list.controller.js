'use strict';

angular.module('burnsearchApp')
    .controller('EntityListController', function($scope, $http, $state, $stateParams, $window, $timeout, entityGetUrl, entityType, mode) {
        $timeout(function () {
            angular.element('#searchFormInput').blur();
        });
        $scope.pageChanged = function () {
            $window.scrollTo(0,0);
            var params = {};
            params["q"] = $stateParams.q;
            params[entityType + "PageNum"] = ($scope.currentPage - 1);
            $state.go(entityType + mode, params);
        };
        $scope.$parent.entityTab = entityType;
        $scope.entityType = entityType;
        $scope.isLoading = true;
        $window.scrollTo(0,0);
        $scope.$parent.listPromise = $http.get(entityGetUrl).then(
            function(response) {
                $scope.currentPage = (+$stateParams[entityType + "PageNum"]) + 1;
                $scope.entities = response.data.content;
                $scope.totalEntities = response.data.totalItems;
                $scope.isLoading = false;
            },
            function (response) {
                $scope.isLoading = false;
            }
        );
    });
