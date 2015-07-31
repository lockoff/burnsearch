'use strict';

/**
 * The card directive displays a Bootstrap panel containing information about an entity (e.g. camp,
 * event). The directive will use a different template depending on the entity type, and the
 * templates should expect a variable named 'entity' to be in scope. 'entity' should be
 * a JSON object of information about the entity. The directive gets its own child
 * scope used to hold information for UI controls if needed (e.g. current tab on the card).
 */
angular.module('burnsearchApp')
    .directive('entityCard', function($compile, $http, $templateCache, Auth) {
        return {
            scope: true,
            restrict: 'E',
            replace: true,
            link: function($scope, $el, $attrs) {
                $scope.$watch($attrs.entityType, function(value) {
                    if (value) {
                        loadTemplate(value);
                    }
                });

                $scope.isAddedToList = false;
                function loadTemplate(entityType) {
                    var templateUrl = undefined;
                    if (entityType == 'events') {
                        templateUrl =  'scripts/components/event/event.directive.template.html';
                        $scope.eventInfoTab = 'Description';
                    }
                    if (entityType == 'camps') {
                        templateUrl = 'scripts/components/camp/camp.directive.template.html';
                    }
                    $scope.addToList = addToList(entityType);
                    $http.get(templateUrl, { cache: $templateCache})
                        .success(function(templateContent) {
                            $el.replaceWith($compile(templateContent)($scope));
                        })
                }

                function addToList(entity) {
                    return function() {
                        Auth.authenticateAction(false);
                        $http.put("/api/list/" + entity + "/" + $scope.entity.id).then(
                            function(response) {
                                $scope.isAddedToList = true;
                            }
                        );
                    }
                }
            }
        }
    });
