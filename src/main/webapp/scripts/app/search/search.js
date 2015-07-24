'use strict';

angular.module('burnsearchApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('search', {
                abstract: true,
                parent: 'site'
            });
    });
