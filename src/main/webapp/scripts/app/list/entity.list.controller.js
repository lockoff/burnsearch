'use strict';

angular.module('burnsearchApp')
    .controller('EntityListController', function($scope, $state, $stateParams, $window, resultsPage, entityType) {
        $scope.pageChanged = function () {
            var params = {};
            params["q"] = $stateParams.q;
            params[entityType + "PageNum"] = ($scope.currentPage - 1);
            $state.go(entityType, params);
            $window.scrollTo(0,0);
        };
        $scope.currentPage = resultsPage.pageNumber + 1;
        $scope.totalEntities = resultsPage.totalEntities;
        $scope.entities = resultsPage.entities;
        $scope.$parent.entityTab = entityType;
        $scope.entityType = entityType;
    });
