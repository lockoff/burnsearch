'use strict';

/**
 * The URL for this state takes a search phrase as a query string parameter. We use this to perform
 * an events query, the results of which will be injected into this view's controller.
 */
angular.module('burnsearchApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('eventSearch', {
                parent: 'search',
                url: '/events/search?q',
                data: {
                    roles: []
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/search/event/event.search.html',
                        controller: 'EventSearchController'
                    }
                },
                resolve: {
                    eventResults: ['$http', '$stateParams', function ($http, $stateParams) {
                        return $http.get('/api/events/search/description?q=' + $stateParams.q).then(
                            function(response) {
                                return response.data;
                            }
                        )
                    }]
                }
            });
    });
