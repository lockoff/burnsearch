'use strict';

angular.module('burnsearchApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('camps', {
                parent: 'search',
                data: {
                    roles: []
                },
                views: {
                    "camps@search": {
                        templateUrl: 'scripts/app/search/camp/camp.search.html',
                        controller: 'CampSearchController'
                    }
                }
            });
    });
