'use strict';

angular.module('burnsearchApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('eventsSearch', {
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
                    entityGetUrl: function($stateParams) {
                        return '/api/events/search/description?q=' + $stateParams.q + "&page=" + $stateParams.eventsPageNum;
                    },
                    entityType: function() { return 'events' }
                }
            });
    });
