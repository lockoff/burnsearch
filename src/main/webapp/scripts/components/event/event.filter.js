'use strict';

angular.module('burnsearchApp')
    .filter('locatedAtArt', function () {
        return function(locatedAtArt) {
            var locationString  = undefined;
            if (locatedAtArt.locationString && locatedAtArt.locationString != "Unknown") {
                locationString = " (" + locatedAtArt.locationString + ")";
            } else {
                locationString = "";
            }
            return locatedAtArt.name + locationString;
        }
    })
    .filter('description', function() {
        return function(description) {
            if (!description || description == 'None') {
                return "No description available.";
            }
            return description;
        }
    });
