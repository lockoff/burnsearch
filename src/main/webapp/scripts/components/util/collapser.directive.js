'use strict';

angular.module('burnsearchApp')
    .directive('collapser', function () {
        return {
            restrict: 'A',
            link: function($scope, $el, $attr) {
                $el.bind('click', function (event) {
                    angular.element('.navbar-collapse').collapse('hide');
                });
            }
        }
    });
