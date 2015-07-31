'use strict';

angular.module('burnsearchApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('campsPlan', {
                parent: 'plan',
                url: '/plan/camps?campsPageNum&eventsPageNum',
                data: {
                    roles: ['ROLE_USER']
                },
                views: {
                    "camps@plan": {
                        templateUrl: 'scripts/app/list/entity.list.html',
                        controller: 'EntityListController'
                    }
                },
                resolve: {
                    resultsPage: function($http, $stateParams) {
                        return $http.get('/api/list/camps/docs?page=' + $stateParams.campsPageNum).then(
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
