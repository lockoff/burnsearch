'use strict';

angular.module('burnsearchApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('etl', {
                parent: 'admin',
                url: '/etl',
                data: {
                    roles: ['ROLE_ADMIN'],
                    pageTitle: 'ETL Launcher'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/admin/etl/etl.html',
                        controller: 'EtlController'
                    }
                },
                resolve: {

                }
            });
    });
