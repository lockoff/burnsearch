'use strict';

angular.module('burnsearchApp')
    .controller('EventController', function($scope, event) {
        $scope.event = event;
        if (event) {
            $scope.showInfoTable = event.url || event.hostingCamp || event.locatedAtArt;
        }
        if (event.locatedAtArt) {
            var locationString  = undefined;
            if (event.locatedAtArt.locationString && event.locatedAtArt.locationString != "Unknown") {
                locationString = "(" + event.locatedAtArt.locationString + ")";
            } else {
                locationString = "";
            }
            $scope.event.locatedAtArtDisplay = event.locatedAtArt.name + " " + locationString;
        }
        if (!event.description || event.description == 'None') {
            event.description = "No description available.";
        }
    });
