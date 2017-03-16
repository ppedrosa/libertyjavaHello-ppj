app.controller('loginCtrl', function($scope, $location, $state, $http, $rootScope, $timeout, demoCatalogService, CONSTANTS,EncryptService, authenticateFactory,authorizeFactory) {
	//$rootScope.$broadcast(CONSTANTS.appConfig.showLoader, false);
	//$scope.rightMenus = ["finance","health","products","communications","resources","crossIndustry"];
	
	$scope.user_message = null;
	$scope.clearMsg = function(){
		$scope.user_message = null;
	};
	
	$rootScope.loginSuccessful=false;
	$rootScope.isAdmin=false;
	var userName = localStorage.getItem("UserName");
	if(userName != null || userName != undefined){
		var decryptedUserName = CryptoJS.AES.decrypt(userName, "sample@user");
		$scope.uid = decryptedUserName.toString(CryptoJS.enc.Utf8);
	}
	var userPassword = localStorage.getItem("Password");
	
	if(userPassword != null || userPassword != undefined){
		var decryptedUserPassword = CryptoJS.AES.decrypt(userPassword, "sample@pwd");
		$('.saveUser, .saveUser1').prop('checked', true);
		$scope.pswd = decryptedUserPassword.toString(CryptoJS.enc.Utf8);
	}else{
		$('.saveUser, .saveUser1').prop('checked', false);
	}
	$scope.chkme = function (id) {
		if($("."+id).is(':checked')){
			$('.saveUser, .saveUser1').prop('checked', true);
		}else{
			$('.saveUser, .saveUser1').prop('checked', false);
		};
		console.log($('.saveUser').is(':checked'));
		console.log($('.saveUser1').is(':checked'));
	};
	//decryptedLocalStorage();
	$scope.submitLogin = function(uid,pswd){
		//loadingImage.show();
		//alert('submit login called');
		$rootScope.result = EncryptService.unameEncrypt(uid, pswd);
			$scope.uid = uid; $scope.pswd = pswd;
			var encryptedUserName = CryptoJS.AES.encrypt($scope.uid, "sample@user");
	    	localStorage.setItem("UserName", encryptedUserName);
	    	var encryptedPwd = CryptoJS.AES.encrypt($scope.pswd, "sample@pwd");
	    	if($(".saveUser").is(':checked') || $(".saveUser1").is(':checked')){ 
	    		localStorage.setItem("Password", encryptedPwd);
	    	} else{
	    		localStorage.removeItem("Password");
	    		$('.saveUser, .saveUser1').prop('checked', false);
	    	}
	    	authenticateFactory.userAuthenticate(function(dataResponse) {
	    		//alert(dataResponse);
	    		//console.log(dataResponse);
	    		if(dataResponse === "Authentication Successful"){
	    			$rootScope.loginSuccessful=true;
					$state.go("catalog-home");	
					$location.path('#/catalog-home');
	    		}
	    		else{
	    			$scope.user_message = "Please enter valid username and password";
					$state.go("catalog-landing");
					$location.path('#/catalog-landing');
	    		}
	    	});
	    	
	    	authorizeFactory.userAuthorize(function(dataResponse1) {
				if(dataResponse1 === "Authorization Successful"){
					$rootScope.isAdmin=true;
					//$state.go("catalog-home");
				}else{
					$rootScope.isAdmin=false;
					//$scope.user_message = "You are not an authorized user";
					//$state.go("catalog-landing");
				}
				
			});	
			
		    $rootScope.navBarPos={
		    	    "top":"45px"
		    };
	};
	
    /*$scope.$on('$viewContentLoaded', function() {
    	$timeout(function(){
    	$(".scroll-menus").hide();
    	$(".scroll-menus1, .scroll-menus1 div").show();
        $(".scroll-menus1").css("top", $(".carousel-inner").height() + 73 + "px").show();
    	}, 2000);
    });*/
});