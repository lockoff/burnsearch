'use strict';

angular.module('burnsearchApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('campsSearch', {
                parent: 'search',
                url: '/search/camps?q&campsPageNum&eventsPageNum',
                data: {
                    roles: []
                },
                views: {
                    "camps@search": {
                        templateUrl: 'scripts/app/list/entity.list.html',
                        controller: 'EntityListController'
                    }
                },
                resolve: {
                    resultsPage: function($http, $stateParams) {
                        return $http.get('/api/camps/search/description?q=' + $stateParams.q + "&page=" + $stateParams.campsPageNum).then(
                            function(response) {
                                return {
                                    entities: response.data.content,
                                    totalEntities: response.data.totalItems,
                                    pageNumber: +$stateParams.campsPageNum
                                }
                            }
                        )
                    },
                    entityType: function() { return 'camps' }
                }

            });
    });
