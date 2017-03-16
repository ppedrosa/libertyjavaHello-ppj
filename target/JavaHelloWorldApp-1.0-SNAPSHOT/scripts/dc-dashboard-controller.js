"use strict";

app.controller('addAppCtrl', function($scope, $location, $state, $stateParams,
		$http, $timeout, demoCatalogService, CONSTANTS, $rootScope) {
	//alert($rootScope.loginSuccessful);
	/*if($rootScope.loginSuccessful===undefined){
		$state.go("catalog-landing");
	}*/
	$(".scroll-menus1").hide();
	$(".scroll-menus").hide();
	$scope.editAppId = $state.params.appId;
	$scope.isEdit = $state.params.appId && $state.params.appId !== "0"? true : false;
	
	$scope.headerText = $scope.isEdit ? CONSTANTS.editApplication.pageHeading : CONSTANTS.createApplication.pageHeading;
	$scope.buttonText = $scope.isEdit ? CONSTANTS.editApplication.submitBtnText : CONSTANTS.createApplication.submitBtnText;
	
	$scope.action = $scope.isEdit ? "/DashboardServlet?appId=" + $scope.editAppId : "/DashboardServlet";
	
	$scope.opGrpNames = CONSTANTS.appConfig.opGrpNames;
	$scope.deleteAppName = "";
	$rootScope.$broadcast(CONSTANTS.appConfig.showLoader, false);
		
	$scope.dc = {
		appName : "",
		opGrp : "",
		shortDesc : "",
		challenge : "",
		solution : "",
		delivery : "",
		docURL : "",
		videoURL : "",
		appURL : "",
		appCred : "",
		image : "",
		priority: "",
		technologyArea: ""
	};

	$scope.resetAddAppForm = function () {
		if ($scope.addAppForm) {
			$scope.dc = {};
			$('#image').val('');
			$('input[type="url"]').val('');
			$('input[type="number"]').val('');
	        $scope.addAppForm.$setPristine();
	    }
	};
	
	$scope.displayDashboardApps = function () {
		$rootScope.$broadcast(CONSTANTS.appConfig.showLoader, true);
		demoCatalogService.getDashboardAppList().then(function(data) {
			if (data.status === CONSTANTS.responseStatusCodes.SUCCESS) {
				$scope.dashboardApps = data.response.appDetails;
			}
			$timeout(function() {
		    	$('.tbldemo-catalog-dashboard').DataTable();
		    }, 2000);
			$rootScope.$broadcast(CONSTANTS.appConfig.showLoader, false);
		});
	};
	
	$scope.addNewApp = function() {
	    $state.go('add-app-details');
	  };
	  
	  $scope.openEditAppPage = function(appId) {
		$state.go('edit-app-details', {appId: appId});
	  };
	  
	  $scope.editApp = function() {
          if ($state.params.appId && $state.params.appId !== "0") {
        	  $rootScope.$broadcast(CONSTANTS.appConfig.showLoader, true);
        	  demoCatalogService.getSingleAppDetails($state.params.appId).then(function(data) {
        		  if (data.status === CONSTANTS.responseStatusCodes.SUCCESS) {
        			  $scope.dc = angular.copy(data.response.appDetails[0]);
        		  }
        		  $('.tbldemo-catalog-dashboard').DataTable();
        		  $rootScope.$broadcast(CONSTANTS.appConfig.showLoader, false);
        	  });  
          }
	  }
	  
	  $scope.openDeleteDialog = function (appName, appId) {
		  $scope.deleteAppName = appName;
		  $scope.deleteAppId = appId;
	  };
	  
	  $scope.deleteApp = function () {
		  demoCatalogService.deleteApplication($scope.deleteAppId).then(function(data) {
			  if(data.response === "success" && data.status === CONSTANTS.responseStatusCodes.SUCCESS) {
				  $scope.displayDashboardApps();
				  $scope.refreshAfterDelete(CONSTANTS.errorSuccessMessages.DEL_SUCCESS_MSG, true);
			  } else {
				  $scope.refreshAfterDelete(CONSTANTS.errorSuccessMessages.DEL_ERROR_MSG, false);
			  }
		  });
	  };
	  
	  $scope.refreshAfterDelete = function (message, isSuccess) {
		  $('#myModal').modal('hide');
		  $(".modal-backdrop").hide();
		  $scope.displayErrorSuccessMsg(message, isSuccess);
	  }
	  
	  $scope.displayErrorSuccessMsg = function (message, isSuccess) {
			var removeClassName = "successDashboard"; 
			var addClassName = "errorDashboard";
			if (isSuccess) {
				removeClassName = "errorDashboard"; 
				addClassName = "successDashboard";  
			}
			$("#errorSuccessMessage").text(message).removeClass(removeClassName).addClass(addClassName);
			$("#errorSuccessMessage").css('display', 'inline-block');
		    $("#errorSuccessMessage").show();
		    $timeout(function() {
		    	$('.tbldemo-catalog-dashboard').DataTable();
		      $("#errorSuccessMessage").hide();
		    }, 5000);
	  };
	  
	  $scope.displayDashboardApps();
	  $scope.editApp();
});