'use strict';

angular.module('burnsearchApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('events', {
                parent: 'search',
                url: '/search/events?q',
                data: {
                    roles: []
                },
                views: {
                    "events@search": {
                        templateUrl: 'scripts/app/search/event/event.search.html',
                        controller: 'EventSearchController'
                    }
                }
            });
    });
