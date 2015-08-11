'use strict';

angular.module('burnsearchApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('eventsSearch', {
                parent: 'search',
                url: '/search/events?q&eventsPageNum&campsPageNum',
                data: {
                    roles: [],
                    pageTitle: "Event Search"
                },
                views: {
                    "events@search": {
                        templateUrl: 'scripts/app/list/entity.list.html',
                        controller: 'EntityListController'
                    }
                },
                resolve: {
                    entityGetUrl: function($stateParams, $httpParamSerializer) {
                        var getParams = {
                            q: $stateParams.q,
                            page: $stateParams.eventsPageNum
                        };
                        return '/api/events/search?' + $httpParamSerializer(getParams);
                    },
                    entityType: function() { return 'events' }
                }
            });
    });
