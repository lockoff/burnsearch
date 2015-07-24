'use strict';

angular.module('burnsearchApp')
    .controller('EventSearchController', function($scope, eventResults) {
        console.log("Here i am!");
        console.log(eventResults);
        $scope.eventResults = eventResults.content;
        //$scope.eventPages = getPages(eventResults.currentPage, eventResults.totalPages);
        //$scope.campResults = campResults;
        //function getPages(currentPage, totalPages) {
        //    var pageSetLen = 10;
        //    var first = 0;
        //    if (currentPage >= (pageSetLen / 2 + 1)) {
        //        first = currentPage - pageSetLen;
        //    }
        //    var last = currentPage + (pageSetLen / 2 - 1);
        //    if (last < pageSetLen - 1) {
        //        last = pageSetLen - 1;
        //    }
        //    if (last >= totalPages) {
        //        last = totalPages - 1;
        //    }
        //    var pages = [];
        //    for (var i = first; i < totalPages; i++) {
        //        pages.push(i+1);
        //    }
        //    return pages;
        //}
    });

