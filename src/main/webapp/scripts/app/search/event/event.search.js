'use strict';

angular.module('burnsearchApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('events', {
                parent: 'search',
                url: '/search/events?q&eventsPageNum&campsPageNum',
                data: {
                    roles: []
                },
                views: {
                    "events@search": {
                        templateUrl: 'scripts/app/list/entity.list.html',
                        controller: 'EntityListController'
                    }
                },
                resolve: {
                    resultsPage: function($http, $stateParams) {
                        return $http.get('/api/events/search/description?q=' + $stateParams.q + "&page=" + $stateParams.eventsPageNum).then(
                            function(response) {
                                return {
                                    entities: response.data.content,
                                    totalEntities: response.data.totalItems,
                                    pageNumber: +$stateParams.eventsPageNum
                                }
                            }
                        )
                    },
                    entityType: function() { return 'events' }
                }
            });
    });
