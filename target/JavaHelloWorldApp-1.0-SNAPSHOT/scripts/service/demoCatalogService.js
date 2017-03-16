"use strict";

app.service("demoCatalogService", function($http, $q) {
	var self = this;
	/**
	 * Function for retrieving landing page details of slider and applications.
	 * @param {String} enterpriseID
	 * @param {String} password
	 * @return
	 */
	self.getLandingPageDetails = function() {
		var deferred = $q.defer();
		$http.get("/DemoCatalogServlet").success(function(data, status) {
			deferred.resolve({
				response : data,
				status : status
			});
		}).error(function(data, status) {
			deferred.resolve({
				response : data,
				status : status
			});
		});
		return deferred.promise;
	}
	
	
	self.getAllDataDetails = function() {
		var deferred = $q.defer();
		$http.get("/DemoCatalogServletTechArea").success(function(data, status) {
			deferred.resolve({
				response : data,
				status : status
			});
		}).error(function(data, status) {
			deferred.resolve({
				response : data,
				status : status
			});
		});
		return deferred.promise;
	}
	
	/**
	 * Function for retrieving app details based on the selected operating group.
	 * @param {String} optGroup
	 */
	self.getAppDetailsBasedOnGroup = function(optGroup) {
		var deferred = $q.defer();
		$http.post("/DemoCatalogServlet?catalog=" + optGroup).success(function(data, status) {
			deferred.resolve({
				response : data,
				status : status
			});
		}).error(function(data, status) {
			deferred.resolve({
				response : data,
				status : status
			});
		});
		return deferred.promise;
	}
	
	/**
	 * Function for retrieving all application details for dashboard page.
	 */
	self.getDashboardAppList = function() {
		var deferred = $q.defer();
		$http.get("/DashboardServlet").success(function(data, status) {
			deferred.resolve({
				response : data,
				status : status
			});
		}).error(function(data, status) {
			deferred.resolve({
				response : data,
				status : status
			});
		});
		return deferred.promise;
	}
	
	/**
	 * Function for retrieving selected app details.
	 * @param {String} appId
	 */
	self.getSingleAppDetails = function(appId) {
		var deferred = $q.defer();
		$http.get("/DashboardServlet?appId=" + appId).success(function(data, status) {
			deferred.resolve({
				response : data,
				status : status
			});
		}).error(function(data, status) {
			deferred.resolve({
				response : data,
				status : status
			});
		});
		return deferred.promise;
	};
	
	/**
	 * Function for deleting selected app.
	 * @param {String} appId
	 */
	self.deleteApplication = function (appId) {
		var deferred = $q.defer();
		$http({
            method: "DELETE",
            url: "/DashboardServlet?appId=" + appId
            
        }).success(function(data, status) {
            deferred.resolve({response: data, status: status});
        }).error(function(data, status) {
            deferred.resolve({status: false});
        })
        return deferred.promise;
	};
});