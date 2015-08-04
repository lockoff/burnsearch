'use strict';

angular.module('burnsearchApp')
    .directive('progressIndicator', function ($rootScope) {
        return {
            restrict:'E',
            template:"<h3 ng-if='isStateLoading'>Loading...</h3>",
            link:function (scope, elem, attrs) {
                scope.isStateLoading = false;

                $rootScope.$on('$stateChangeStart', function(){
                    scope.isStateLoading = true;
                });

                $rootScope.$on('$stateChangeSuccess', function(){
                    scope.isStateLoading = false;
                });
            }
        };
    });