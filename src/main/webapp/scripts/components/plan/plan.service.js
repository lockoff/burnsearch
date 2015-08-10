'use strict';

angular.module('burnsearchApp')
    .factory('PlanService', function ($http, $q, Auth, Principal, StorageService) {

        function isInPlan(planType, entityId) {
            if (!Principal.isAuthenticated()) {
                var deferred = $q.defer();
                deferred.resolve(false);
                return deferred.promise;
            }
            return $http.get("/api/plan/" + planType + "/contains/" + entityId).then(
                function(response) {
                    return response.data;
                }
            )
        }

        function addToPlan(planType, entityId) {
            Auth.authenticateAction(false);
            return $http.put("/api/plan/" + planType + "/" + entityId);
        }

        function removeFromPlan(planType, entityId) {
            Auth.authenticateAction(false);
            return $http.delete("/api/plan/" + planType + "/" + entityId);
        }

        return {
            isInPlan: isInPlan,
            addToPlan: addToPlan,
            removeFromPlan: removeFromPlan
        }
    });
