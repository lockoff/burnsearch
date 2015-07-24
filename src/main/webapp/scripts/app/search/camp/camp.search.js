'use strict';

/**
 * The URL for this state takes a search phrase as a query string parameter. We use this to perform
 * an camps query, the results of which will be injected into this view's controller.
 */
angular.module('burnsearchApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('campSearch', {
                parent: 'search',
                url: '/camps/search?q',
                data: {
                    roles: []
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/search/camp/camp.search.html',
                        controller: 'CampSearchController'
                    }
                },
                resolve: {
                    campResults: ['$http', '$stateParams', function ($http, $stateParams) {
                        return $http.get('/api/camps/search/description?q=' + $stateParams.q).then(
                            function(response) {
                                return response.data;
                            }
                        )
                    }]
                }
            });
    });
