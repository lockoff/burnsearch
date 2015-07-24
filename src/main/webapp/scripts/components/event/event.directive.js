'use strict';

/**
 * The event-card directive displays a Bootstrap panel containing information about an event.
 * The template expects a variable named 'event' to be in the parent scope. 'event' should be a JSON
 * object of event information retrieved from Elasticsearch. The directive gets its own child
 * scope used to hold information for UI controls (e.g. the current tab in the event card).
 */
angular.module('burnsearchApp')
    .directive('eventCard', function() {
        return {
            scope: true,
            restrict: 'E',
            replace: true,
            templateUrl: 'scripts/components/event/event.directive.template.html',
            link: function($scope, $el, $attrs) {
                $scope.eventInfoTab = 'Description';
            }
        }
    });
