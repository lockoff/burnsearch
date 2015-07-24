'use strict';

angular.module('burnsearchApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('search', {
                parent: 'site',
                url: "/search?q",
                data: {
                    roles: []
                },
                views: {
                    "content@": {
                        templateUrl: 'scripts/app/search/search.html',
                        controller: 'SearchController'
                    }
                },
                resolve: {
                    eventResults: ['$http', '$stateParams', function ($http, $stateParams) {
                        return $http.get('/api/events/search/description?q=' + $stateParams.q).then(
                            function(response) {
                                return response.data;
                            }
                        )
                    }],
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
