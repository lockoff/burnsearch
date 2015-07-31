'use strict';

angular.module('burnsearchApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('eventsPlan', {
                parent: 'plan',
                url: '/plan/events?eventsPageNum&campsPageNum',
                data: {
                    roles: ['ROLE_USER']
                },
                views: {
                    "events@plan": {
                        templateUrl: 'scripts/app/list/entity.list.html',
                        controller: 'EntityListController'
                    }
                },
                resolve: {
                    resultsPage: function($http, $stateParams) {
                        return $http.get('/api/list/events/docs?page=' + $stateParams.eventsPageNum).then(
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
