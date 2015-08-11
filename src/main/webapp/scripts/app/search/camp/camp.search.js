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
                    entityGetUrl: function($stateParams, $httpParamSerializer) {
                        var getParams = {
                            q: $stateParams.q,
                            page: $stateParams.campsPageNum
                        };
                        return '/api/camps/search?' + $httpParamSerializer(getParams);
                    },
                    entityType: function() { return 'camps' }
                }

            });
    });
