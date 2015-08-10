'use strict';

/**
 * The URL for this state takes the ID of a camp as a parameter. We get the data for the camp with
 * that ID and pass it to the controller. If the camp could not be found, the empty string is
 * returned.
 */
angular.module('burnsearchApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('camp', {
                parent: 'site',
                url: '/camp/:campId',
                data: {
                    roles: [],
                    pageTitle: "Camp"
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/camp/camp.html',
                        controller: 'CampController'
                    }
                },
                resolve: {
                    camp: ['$http', '$stateParams', function ($http, $stateParams) {
                        return $http.get('/api/camps/' + $stateParams.campId).then(
                            function(response) {
                                return response.data;
                            }
                        )
                    }]
                }
            });
    });
