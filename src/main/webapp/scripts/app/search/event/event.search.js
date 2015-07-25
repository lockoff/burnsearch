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
                        templateUrl: 'scripts/app/search/event/event.search.html',
                        controller: 'EventSearchController'
                    }
                },
                resolve: {
                    eventsPage: function($http, $stateParams) {
                        return $http.get('/api/events/search/description?q=' + $stateParams.q + "&page=" + $stateParams.eventsPageNum).then(
                            function(response) {
                                return {
                                    events: response.data.content,
                                    totalEvents: response.data.totalItems,
                                    pageNumber: +$stateParams.eventsPageNum
                                }
                            }
                        )
                    }
                }
            });
    });
