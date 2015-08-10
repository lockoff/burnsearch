'use strict';

angular.module('burnsearchApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('campsSearch', {
                parent: 'search',
                url: '/search/camps?q&campsPageNum&eventsPageNum',
                data: {
                    roles: [],
                    pageTitle: "Camp Search"
                },
                views: {
                    "camps@search": {
                        templateUrl: 'scripts/app/list/entity.list.html',
                        controller: 'EntityListController'
                    }
                },
                resolve: {
                    entityGetUrl: function($stateParams) {
                        return '/api/camps/search/description?q=' + $stateParams.q + "&page=" + $stateParams.campsPageNum;
                    },
                    entityType: function() { return 'camps' }
                }

            });
    });
