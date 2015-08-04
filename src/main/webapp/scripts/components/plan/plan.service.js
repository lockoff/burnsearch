'use strict';

angular.module('burnsearchApp')
    .factory('PlanService', function ($http, $q, Auth, Principal, StorageService) {
        function loadPlans() {
            var plans = {
                events: undefined,
                camps: undefined
            };
            return loadPlan("events").then(function (eventsPlan) {
                plans.events = eventsPlan;
                return loadPlan("camps").then(function(campsPlan) {
                    plans.camps = campsPlan;
                    StorageService.save("plans", plans);
                })
            })
        }

        function loadPlan(planType) {
            return $http.get("/api/plan/" + planType).then(
                function (response) {
                    var plan = {};
                    response.data.forEach(function (entityId) {
                        plan[entityId] = true
                    });
                    return plan;
                }
            )
        }

        function getPlanMapValue(planType, entityId) {
            var plans = StorageService.get("plans");
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
            if (!StorageService.get("plans")) {
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
                    var plans = StorageService.get("plans");
                    plans[planType][entityId] = true;
                    StorageService.save("plans", plans);
                }
            );
        }

        function removeFromPlan(planType, entityId) {
            Auth.authenticateAction(false);
            return $http.delete("/api/plan/" + planType + "/" + entityId).then(
                function (response) {
                    var plans = StorageService.get("plans");
                    plans[planType][entityId] = false;
                    StorageService.save("plans", plans);
                }
            );
        }

        return {
            clearPlans: function () {
                StorageService.remove("plans");
            },
            isInPlan: isInPlan,
            addToPlan: addToPlan,
            removeFromPlan: removeFromPlan
        }
    });
