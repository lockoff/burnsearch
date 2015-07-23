'use strict';

angular.module('burnsearchApp')
    .filter('dateString', function () {
       return function(timeMillis) {
           var dateTime = new Date(timeMillis);
           return dateTime.toDateString();
       }
    })
    .filter('time', function() {
        return function(timeMillis) {
            var dateTime = new Date(timeMillis);
            var options = {timeZone: 'America/Los_Angeles'};
            return dateTime.toLocaleTimeString("en-US", options);
        }
    });
