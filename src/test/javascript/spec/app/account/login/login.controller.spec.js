'use strict';

describe('Controllers Tests ', function () {

    beforeEach(module('burnsearchApp'));

    describe('LoginController', function () {
        var $scope;


        beforeEach(inject(function ($rootScope, $controller) {
            $scope = $rootScope.$new();
            $controller('LoginController', {$scope: $scope});
        }));
    });
});
