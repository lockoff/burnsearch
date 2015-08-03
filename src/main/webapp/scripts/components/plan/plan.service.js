'use strict';

angular.module('burnsearchApp')
    .factory('PlanService', function ($http, $q, Auth, Principal) {
        var plans = {
            events: undefined,
            camps: undefined
        };

        function loadPlans() {
            return loadPlan("events").then(function () {
                return loadPlan("camps")
            })
        }

        function loadPlan(planType) {
            return $http.get("/api/plan/" + planType).then(
                function (response) {
                    var plan = {};
                    response.data.forEach(function (entityId) {
                        plan[entityId] = true
                    });
                    plans[planType] = plan;
                    return plans;
                }
            )
        }

        function getPlanMapValue(planType, entityId) {
            if (plans[planType][entityId]) {
                return true;
            }
            return false;
        }

        function isInPlan(planType, entityId) {
            var deferred = $q.defer();
            var promise = deferred.promise;
            if (!Principal.isAuthenticated()) {
                deferred.resolve(false);
                return promise;
            }
            if (!plans.events && !plans.camps) {
                return loadPlans().then(function () {
                    return getPlanMapValue(planType, entityId);
                });
            }
            deferred.resolve(getPlanMapValue(planType, entityId));
            return promise;
        }

        function addToPlan(planType, entityId) {
            Auth.authenticateAction(false);
            return $http.put("/api/plan/" + planType + "/" + entityId).then(
                function (response) {
                    plans[planType][entityId] = true;
                }
            );
        }

        function removeFromPlan(planType, entityId) {
            Auth.authenticateAction(false);
            return $http.delete("/api/plan/" + planType + "/" + entityId).then(
                function (response) {
                    plans[planType][entityId] = false;
                }
            );
        }

        return {
            clearPlans: function () {
                plans.events = undefined;
                plans.camps = undefined;
            },
            isInPlan: isInPlan,
            addToPlan: addToPlan,
            removeFromPlan: removeFromPlan
        }
    });
