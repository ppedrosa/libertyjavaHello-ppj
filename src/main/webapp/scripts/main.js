// angular.module is a global place for creating, registering and retrieving Angular modules

var app = angular.module('catalogApp', ['ui.router']);

app.config(function($stateProvider, $urlRouterProvider, $locationProvider) {
	$stateProvider
        .state('catalog-landing', {
            url: '/catalog-landing',
            templateUrl: 'views/catalog-login.html',
			controller: 'loginCtrl'
        })
        .state('catalog-home', {
	        url: '/catalog-home',
	        templateUrl: 'views/catalog-home.html',
	        controller: 'homeCtrl'
	    })
        .state('catalog-details', {
            url: '/catalog-details?catalog&appN',
            templateUrl: 'views/catalog-details.html',
			controller: 'catalogCtrl'
        })
        .state('catalog-details1', {
            url: '/catalog-techs?catalog1',
            templateUrl: 'views/catalog-home2.html',
			controller: 'catalogCtrl1'
        })
        .state('add-app-details', {
            url: '/add-app-details',
            templateUrl: 'views/add-edit-app-details.html',
			controller: 'addAppCtrl'
        })
        /*.state('demo-catalog-dashboard', {
            url: '/demo-catalog-dashboard',
            templateUrl: 'views/demo-catalog-dashboard.html',
			controller: 'addAppCtrl'
        })*/
        .state('demo-catalog-admin-console', {
            url: '/demo-catalog-admin-console',
            templateUrl: 'views/demo-catalog-dashboard.html',
			controller: 'addAppCtrl'
        })
       .state('edit-app-details', {
            url: '/edit-app-details?appId',
            templateUrl: 'views/add-edit-app-details.html',
			controller: 'addAppCtrl'
        });
	
	$urlRouterProvider.otherwise('/catalog-landing');
	$locationProvider.html5Mode({
		  enabled: true,
		  requireBase: false
		});
});

app.factory('authenticateFactory', function($http, $rootScope){
    return{
    	userAuthenticate: function(callbackFunc){
    		$http(
    				{
    					method : 'Get',
    					url : "https://eso-authenticator.mybluemix.net/AuthenticationService?username="+$rootScope.result[0]+"&tokenid="+$rootScope.result[1],
    					//params: {"username" :$rootScope.result[0], "tokenid" :$rootScope.result[1]},
    					cache:true
    				}).success(function(data1) {
    				callbackFunc(data1);
    			}).error(function(error) {
    				alert("An exception has occurred. Please try after some time");
    			});
    	}
    }
 });

app.factory('authorizeFactory', function($http, $rootScope){
    return{
    	userAuthorize: function(callbackFunc){
    		$http(
    				{
    					method : 'Get',
    					url : "/AuthorizationServlet?username="+$rootScope.result[2],
    					//params: {"username" :$rootScope.result[0], "tokenid" :$rootScope.result[1]},
    					cache:true
    				}).success(function(data1) {
    				callbackFunc(data1);
    			}).error(function(error) {
    				alert("An exception has occurred. Please try after some time.");
    			});
    	}
    }
 });

app.service('EncryptService', function(){
    this.unameEncrypt = function(uid, pswd) {
        var rkEncryptionKey = CryptoJS.enc.Base64.parse('u/Gu5posvwDsXUnV5Zaq4g==');
        var rkEncryptionIv = CryptoJS.enc.Base64.parse('5D9r9ZVzEYYgha93/aUK2w==');        
        var userid = CryptoJS.enc.Utf8.parse(uid);
        var encryptedUserId = CryptoJS.AES.encrypt(userid.toString(), rkEncryptionKey, {mode: CryptoJS.mode.CBC, padding: CryptoJS.pad.Pkcs7, iv: rkEncryptionIv});
        encryptedUserId=encodeURIComponent(encryptedUserId.toString());         
        localStorage.setItem("UserName", encryptedUserId);
        var userPwd = CryptoJS.enc.Utf8.parse(pswd);
        var encryptedPassword = CryptoJS.AES.encrypt(userPwd.toString(), rkEncryptionKey, {mode: CryptoJS.mode.CBC, padding: CryptoJS.pad.Pkcs7, iv: rkEncryptionIv});
        encryptedPassword=encodeURIComponent(encryptedPassword.toString());
        localStorage.setItem("Password", encryptedPassword);
        return [encryptedUserId, encryptedPassword,uid];
    };
 });

