'use strict';

angular.module('burnsearchApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('campsPlan', {
                parent: 'plan',
                url: '/plan/camps?campsPageNum&eventsPageNum',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: "Camp Plan"
                },
                views: {
                    "camps@plan": {
                        templateUrl: 'scripts/app/list/entity.list.html',
                        controller: 'EntityListController'
                    }
                },
                resolve: {
                    entityGetUrl: function($stateParams) {
                        return '/api/plan/camps/docs?page=' + $stateParams.campsPageNum;
                    },
                    entityType: function() { return 'camps' }
                }

            });
    });
