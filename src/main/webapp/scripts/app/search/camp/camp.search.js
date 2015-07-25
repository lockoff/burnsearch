'use strict';

angular.module('burnsearchApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('camps', {
                parent: 'search',
                url: '/search/camps?q&campsPageNum&eventsPageNum',
                data: {
                    roles: []
                },
                views: {
                    "camps@search": {
                        templateUrl: 'scripts/app/search/camp/camp.search.html',
                        controller: 'CampSearchController'
                    }
                },
                resolve: {
                    campsPage: function($http, $stateParams) {
                        return $http.get('/api/camps/search/description?q=' + $stateParams.q + "&page=" + $stateParams.campsPageNum).then(
                            function(response) {
                                return {
                                    camps: response.data.content,
                                    totalCamps: response.data.totalItems,
                                    pageNumber: +$stateParams.campsPageNum
                                }
                            }
                        )
                    }
                }

            });
    });
