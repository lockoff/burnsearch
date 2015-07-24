'use strict';

/**
 * The camp-card directive displays a Bootstrap panel containing information about a theme camp.
 * The template expects a variable named 'camp' to be in the scope. 'camp' should be a JSON object
 * of camp information retrieved from Elasticsearch.
 */
angular.module('burnsearchApp')
    .directive('campCard', function() {
        return {
            restrict: 'E',
            replace: true,
            templateUrl: 'scripts/components/camp/camp.directive.template.html'
        }
    });
