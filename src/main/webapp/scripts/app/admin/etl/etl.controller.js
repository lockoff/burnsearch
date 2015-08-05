'use strict';

angular.module('burnsearchApp')
    .controller('EtlController', function ($scope, $http) {
        $scope.etlPromise = undefined;
        $scope.etlResponse = undefined;
        $scope.launchEtl = function () {
            $scope.etlPromise = $http.post("/api/admin/etl").then(
                function(response) {
                    $scope.etlResponse = response;
                },
                function(response) {
                    $scope.etlResponse = response;
                }
            )
        }
    });
