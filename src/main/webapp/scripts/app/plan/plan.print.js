'use strict';

angular.module('burnsearchApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('printPlan', {
                parent: 'plan',
                url: "/plan/print",
                data: {
                    roles: ['ROLE_USER']
                },
                views: {
                    "content@": {
                        templateUrl: 'scripts/app/plan/plan.print.html',
                        controller: 'PlanPrintController'
                    }
                },
                resolve: {
                    campsPlan: function($http) {
                        return $http.get("/api/plan/camps/docs?page=0&size=2147483647").then(
                            function(response) {
                                return response.data.content;
                            }
                        );
                    },
                    eventsPlan: function($http) {
                        return $http.get("/api/plan/events/docs/print").then(
                            function(response) {
                                return response.data;
                            }
                        )
                    }
                }
            });
    });
