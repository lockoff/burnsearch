'use strict';

angular.module('burnsearchApp')
    .factory('PlanService', function ($http, $q, Auth, Principal, StorageService) {
        function loadPlans() {
            var plans = {
                events: undefined,
                camps: undefined
            };
            var planLengths = {
                events: 0,
                camps: 0
            };
            return loadPlan("events").then(function (eventsPlan) {
                plans.events = eventsPlan.plan;
                planLengths.events = eventsPlan.length;
                return loadPlan("camps").then(function(campsPlan) {
                    plans.camps = campsPlan.plan;
                    planLengths.events = campsPlan.length;
                    StorageService.save("plans", plans);
                    StorageService.save("planLengths", planLengths);
                })
            })
        }

        function loadPlan(planType) {
            return $http.get("/api/plan/" + planType).then(
                function (response) {
                    var plan = {};
                    var length = 0;
                    response.data.forEach(function (entityId) {
                        plan[entityId] = true;
                        length = length + 1;
                    });
                    return {
                        plan: plan,
                        length: length
                    }
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
                    var planLengths = StorageService.get("planLengths");
                    planLengths[planType] = planLengths[planType] + 1;
                    StorageService.save("plans", plans);
                    StorageService.save("planLengths", planLengths);
                }
            );
        }

        function removeFromPlan(planType, entityId) {
            Auth.authenticateAction(false);
            return $http.delete("/api/plan/" + planType + "/" + entityId).then(
                function (response) {
                    var plans = StorageService.get("plans");
                    plans[planType][entityId] = false;
                    var planLengths = StorageService.get("planLengths");
                    planLengths[planType] = planLengths[planType] - 1;
                    StorageService.save("plans", plans);
                    StorageService.save("planLengths", planLengths);
                }
            );
        }

        function isPlanEmpty(planType) {
            var planLengths = StorageService.get("planLengths");
            if (planLengths[planType] > 0) {
                return false;
            }
            return true;
        }

        return {
            clearPlans: function () {
                StorageService.remove("plans");
            },
            isInPlan: isInPlan,
            addToPlan: addToPlan,
            removeFromPlan: removeFromPlan,
            isPlanEmpty: isPlanEmpty
        }
    });
