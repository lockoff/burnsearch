'use strict';

angular.module('burnsearchApp')
    .filter('locatedAtArt', function () {
        return function(locatedAtArt) {
            if (!locatedAtArt) return "";
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
    })
    .filter('eventType', function() {
        return function(eventType) {
            if (!eventType || eventType.label == 'None') {
                return 'Event';
            }
            switch(eventType.label) {
                case "Adult-oriented":
                case "Food":
                case "Kid-friendly":
                case "Care/Support":
                    return eventType.label + " Event";
                default:
                    return eventType.label;
            }
        }
    })
    .filter('eventLocation', function() {
        return function(event) {
            if (!event) return "";
            if (event.checkLocation) {
                return "Check at Playa Info";
            }
            if (event.otherLocation) {
                return event.otherLocation;
            }
        }
    });
