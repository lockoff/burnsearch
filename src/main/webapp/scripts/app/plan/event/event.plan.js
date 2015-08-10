'use strict';

angular.module('burnsearchApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('eventsPlan', {
                parent: 'plan',
                url: '/plan/events?eventsPageNum&campsPageNum',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: "Event Plan"
                },
                views: {
                    "events@plan": {
                        templateUrl: 'scripts/app/list/entity.list.html',
                        controller: 'EntityListController'
                    }
                },
                resolve: {
                    entityGetUrl: function($stateParams) {
                        return '/api/plan/events/docs?page=' + $stateParams.eventsPageNum;
                    },
                    entityType: function() { return 'events' }
                }
            });
    });
