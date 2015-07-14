'use strict';

angular.module('burnsearchApp')
    .factory('Register', function ($resource) {
        return $resource('api/register', {}, {
        });
    });


