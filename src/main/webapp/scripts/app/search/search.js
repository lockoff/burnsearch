'use strict';

angular.module('burnsearchApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('search', {
                parent: 'site',
                abstract: true,
                data: {
                    roles: []
                },
                views: {
                    "content@": {
                        templateUrl: 'scripts/app/list/entity.list.shell.html',
                        controller: 'EntityListShellController'
                    }
                }
            });
    });
