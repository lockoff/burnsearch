'use strict';

/**
 * The URL for this state takes the ID of an event as a parameter. We get the data for the event
 * with that ID and pass it to the controller. If the event could not be found, the empty string is
 * returned.
 */
angular.module('burnsearchApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('event', {
                parent: 'site',
                url: '/event/:eventId',
                data: {
                    roles: []
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/event/event.html',
                        controller: 'EventController'
                    }
                },
                resolve: {
                    event: ['$http', '$stateParams', function ($http, $stateParams) {
                        return $http.get('/api/events/' + $stateParams.eventId).then(
                            function(response) {
                                return response.data;
                            }
                        )
                    }]
                }
            });
    });
