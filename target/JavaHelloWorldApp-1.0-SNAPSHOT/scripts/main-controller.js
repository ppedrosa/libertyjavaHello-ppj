app.controller('homeCtrl', function($scope, $location, $state, $http, $window, $rootScope, $timeout, demoCatalogService, CONSTANTS,EncryptService, authenticateFactory,authorizeFactory, $stateParams) {
	//$rootScope.$broadcast(CONSTANTS.appConfig.showLoader, false);
	$scope.rightMenus = ["finance","health","products","communications","resources","crossIndustry"];	
	//alert($rootScope.loginSuccessful);
	$window.scrollTo(0, 0);
	$('.tech-page').hide();
	if($rootScope.loginSuccessful===undefined){
		$state.go("catalog-landing");
	}
	$scope.getLandingPageDetails = function() {
		$rootScope.$broadcast(CONSTANTS.appConfig.showLoader, true);
		$('#preloader').show();
		demoCatalogService.getLandingPageDetails().then(function(data) {
			if (data.status === CONSTANTS.responseStatusCodes.SUCCESS) {
				$scope.carouselContents = data.response.slider;
				console.log(data.response);
				var response = data.response.appDetails;
				var tempResponse = [];
				angular.forEach($scope.rightMenus, function(item) {
					var obj = response.filter(function(data) {return data.homeId == item;});
					if (obj.length > 0) {
						tempResponse.push(obj[0]);
					}
				});
				setTimeout(function () {
					$("a").tooltip();
					$('.tech-page').hide();
					$('.landing-page').show();
					$('.carousel').carousel({
			            interval: 3000
			        });
				}, 2000);
				$scope.homeContents = tempResponse;
				$rootScope.$broadcast(CONSTANTS.appConfig.showLoader, false);
				$('#preloader').hide()
				//$scope.homeContents1 = $scope.homeContents;
				var catalogTitle = '';
				catalogTitle = $stateParams.catalog1;
				console.log("Catalogue Length"+ catalogTitle.length);
				if(catalogTitle.length>0){
					$scope.getUpdates(catalogTitle);
				}
				$rootScope.$broadcast(CONSTANTS.appConfig.showLoader, false);
				$('#preloader').hide()
			} else {
				$rootScope.$broadcast(CONSTANTS.appConfig.showLoader, false);
				$('#preloader').hide()
			}
			//$scope.startC();
		});
	};
	
	
	
	$scope.getUpdates = function (dat) {
		$window.scrollTo(0, 0);
		$rootScope.$broadcast(CONSTANTS.appConfig.showLoader, true);
		$('#preloader').show();
		demoCatalogService.getAppDetailsBasedOnGroup(encodeURIComponent(dat)).then(function(data) {
			if (data.status === CONSTANTS.responseStatusCodes.SUCCESS) {
				console.log(data.response.appDetails);
				$scope.homeContents = '';
				$scope.homeContents = data.response.appDetails;
				$scope.otherItems = [];
				//$rootScope.$broadcast(CONSTANTS.appConfig.showLoader, false);
				setTimeout(function () {
					$("a").tooltip();
					$('.tech-page').hide();
					$('.landing-page').show();
					$rootScope.$broadcast(CONSTANTS.appConfig.showLoader, false);
					$(window).scrollTop($('.home-items').offset().top);
					$('#preloader').hide()
				}, 3000);
			} else {
				$rootScope.$broadcast(CONSTANTS.appConfig.showLoader, false);
				$('#preloader').hide()
			}
		});
	};
	
	$scope.techShow = function (dat) {
		//console.log(dat);
		//console.log($scope.homeContents1);
		$window.scrollTo(0, 0);
		$rootScope.$broadcast(CONSTANTS.appConfig.showLoader, true);
		$('#preloader').show();
		$scope.homeContents = [];
		demoCatalogService.getAllDataDetails().then(function(data) {
			$scope.homeContents2 = [];
			if (data.status === CONSTANTS.responseStatusCodes.SUCCESS) {
				$scope.homeContents2 = data.response.appDetails;
				$.each($scope.homeContents2, function (j, temps) {
					if(temps.technologyArea){
					var list = temps.technologyArea;
					console.log(list);
					var newTemps = [];
					if(list.indexOf(', ') != -1){
						newTemps = temps.technologyArea.split(', ');
						$.each(newTemps, function (l, tmps) {
							if(tmps == dat){
								$scope.homeContents.push($scope.homeContents2[j]);
							}
						});
					}
					
					if(temps.technologyArea == dat){
						$scope.homeContents.push($scope.homeContents2[j]);
					}
					}
				});
				setTimeout(function () {
					$("a").tooltip();
					$('.tech-page').show();
					$('.landing-page').hide();
					$rootScope.$broadcast(CONSTANTS.appConfig.showLoader, false);
					$(window).scrollTop($('.home-items').offset().top);
					$('#preloader').hide();
				}, 3000);
			}else{
				$('.tech-page').hide();
				$rootScope.$broadcast(CONSTANTS.appConfig.showLoader, false);
				$('#preloader').hide();
			}
		});
	}
	$scope.getLandingPageDetails();
	
    var idds = [];
    $scope.iniit = function() {
        $(".scroll-menus").show();
    };
    $(".scroll-menus div a, .scroll-menus1 div a").click(function() {
        $('html, body').animate({
            scrollTop: $($(this).attr("href")).offset().top - 64
        }, 500);
        idds = [];
        
        $scope.scrollFn();
    });
    $(window).on('scroll', function() {
    	if($location.$$path=="/catalog-home"){
    		$scope.scrollFn();
        }
    });
    
    /*$scope.$on('$viewContentLoaded', function() {
    	$timeout(function(){
    	$(".scroll-menus").hide();
    	$(".scroll-menus1, .scroll-menus1 div").show();
        $(".scroll-menus1").css("top", $(".carousel-inner").height() + 73 + "px").show();
    	}, 2000);
    });*/
    
    $scope.scrollFn = function() {
        idds = [];
        $(".sub-drops").hide();
        if ($(window).scrollTop() >= $(".carousel-inner").height()) {
            $(".scroll-menus, .scroll-menus div").show();
            $(".scroll-menus1").hide();
        } else {
            $(".scroll-menus, .scroll-menus div").hide();
            $(".scroll-menus1").show();
        };
        $(".home-items>div").each(function(i, v) {
            if ($(window).scrollTop() >= $(this).offset().top - 80) {
                idds.push($(this).attr("id"));
            }
        });
        if ($("." + idds.slice(-1).pop()).length != 0) {
            switch (idds.slice(-1).pop()) {
                case 'finance':
                    objs = {
                        "finance": 76,
                        "health": 122,
                        "product": 169,
                        "communications": 224,
                        "resources": 272,
                        "crossIndustry": 317
                    };
                    $scope.adjuHeight();
                    break;
                case 'health':
                    objs = {
                        "finance": 72,
                        "health": 118,
                        "product": 167,
                        "communications": 222,
                        "resources": 270,
                        "crossIndustry": 315
                    };
                    $scope.adjuHeight();
                    break;
                case 'products':
                    objs = {
                        "finance": 72,
                        "health": 118,
                        "product": 168,
                        "communications": 224,
                        "resources": 272,
                        "crossIndustry": 320
                    };
                    $scope.adjuHeight();
                    break;
                case 'communications':
                    objs = {
                        "finance": 72,
                        "health": 118,
                        "product": 167,
                        "communications": 223,
                        "resources": 293,
                        "crossIndustry": 342
                    };
                    $scope.adjuHeight();
                    break;
                case 'resources':
                    objs = {
                        "finance": 72,
                        "health": 117,
                        "product": 166,
                        "communications": 221,
                        "resources": 268,
                        "crossIndustry": 320
                    };
                    $scope.adjuHeight();
                    break;
            }
            $("." + idds.slice(-1).pop()).addClass('active').children().find('.span').show();
            $("." + idds.slice(-1).pop()).siblings('div').removeClass('active').children().find('.span').hide();
        } else {
            $(".scroll-menus div").removeClass('active').children().find('.span').hide();
        }
        $(".moreLink").bind("click", function() {
            $r = $(this).siblings('.big-desc');
            $(this).siblings('.big-desc').slideDown(1500);
            $(this).siblings('.small-desc').hide();
            $(this).hide();
            $(this).siblings('.lessLink').show();
        });
        $(".lessLink").bind('click', function() {
            $(this).siblings('.small-desc').show();
            $(this).siblings('.big-desc').slideUp(1500);
            $(this).hide();
            $(this).siblings('.moreLink').show();
        });
    };
    $scope.adjuHeight = function() {
        $(".finance").css("top", objs.finance + "px");
        $(".health").css("top", objs.health + "px");
        $(".products").css("top", objs.product + "px");
        $(".communications").css("top", objs.communications + "px");
        $(".resources").css("top", objs.resources + "px");
        $(".crossIndustry").css("top", objs.crossIndustry + "px");
    };
    $(".scroll-menus>div").each(function() {
        $(this).removeClass('active').children().find('.span').hide();
    });
    $('html, body').animate({
        scrollTop: 0
    }, 50);
    $('.home-menu, .ops-menu, .scroll-menus').hide();
    
    $scope.showtitle = function () {
    	$(".myTooltip").tooltip('toggle');
    };
});

app.controller('catalogCtrl', function($scope, $rootScope, $location, $state, $window, $stateParams, $http, $timeout, demoCatalogService, CONSTANTS) {
	$rootScope.$broadcast(CONSTANTS.appConfig.showLoader, false);
    //console.log($stateParams.catalog);
	$window.scrollTo(0, 0);
    $scope.catalogTitle = $stateParams.catalog;
    $scope.catalogApp = $stateParams.appN;
    $(window).on('scroll', function() {
    	if($location.$$path!="/catalog-home"){
    	$(".scroll-menu, .scroll-menus div, .scroll-menus1 div, .scroll-menus1").hide();
    	}
    });
    $(".scroll-menu, .scroll-menus div, .scroll-menus1 div, .scroll-menus1").hide();
    $scope.$on('$viewContentLoaded', function() {
    	$timeout(function(){
        $(".scroll-menu, .scroll-menus div, .scroll-menus1 div, .scroll-menus1").hide();
        $('.home-menu, .ops-menu').show();
    	}, 30);
    });

    $scope.callBack = function(json) {
       /* $http.get(json).
        success(function(data, status, headers, config) {
            $scope.catelogContents = data;
        }).
        error(function(data, status, headers, config) {
            // log error
        });*/
    	$rootScope.$broadcast(CONSTANTS.appConfig.showLoader, true);
    	$('#preloader').show();
    	demoCatalogService.getAppDetailsBasedOnGroup(json).then(function(data) {
			if (data.status === CONSTANTS.responseStatusCodes.SUCCESS) {
				console.log(data.response.appDetails);
				$scope.catelogContents = data.response.appDetails;
				$scope.otherItems = [];

				$.each($scope.catelogContents, function (i, tem) {
				 if(tem.appName==$scope.catalogApp){
					$scope.title = tem.title;
					$scope.appName = tem.appName;
					$scope.challenge = tem.challenge;
					$scope.solution = tem.solution;
					$scope.delivery = tem.delivery;
					$scope.docLink = tem.docLink;
					$scope.imgsrc =  tem.imgSrc;
					$scope.cred =  tem.cred;
					$scope.appLink =  tem.appLink;
					$scope.techArea1 = [];
					$scope.techArea = [];
					if(tem.technologyArea || tem.technologyArea != 'NA'){
						if(tem.technologyArea.indexOf(', ') != -1){
							$scope.techArea1.push(tem.technologyArea.split(', '));
							$scope.techArea = $scope.techArea1[0];
						}else{
							$scope.techArea.push(tem.technologyArea);
						}
					}
				 }else{
					 $scope.otherItems.push(tem);
				  };
				});
				setTimeout(function () {
					$("a").tooltip();
				}, 2000);
				$rootScope.$broadcast(CONSTANTS.appConfig.showLoader, false);
				$('#preloader').hide();
			} else {
				$rootScope.$broadcast(CONSTANTS.appConfig.showLoader, false);
			}
		});
    };
    switch ($scope.catalogTitle) {
        case "Financial Services":
            $scope.subTitles = "Financial Services";
            $scope.callBack(encodeURIComponent($scope.subTitles));
            break;
        case "Health ":
            $scope.subTitles = "Health & Public Services";
            $scope.callBack(encodeURIComponent($scope.subTitles));
            break;
        case "Products":
            $scope.subTitles = "Products";
            $scope.callBack(encodeURIComponent($scope.subTitles));
            break;
        case "Communications Media ":
            $scope.subTitles = "Communications Media & Technology";
            $scope.callBack(encodeURIComponent($scope.subTitles));
            break;
        case "Resources":
            $scope.subTitles = "Resources";
            $scope.callBack(encodeURIComponent($scope.subTitles));
            break;
        case "Cross Industry":
            $scope.subTitles = "Cross Industry";
            $scope.callBack(encodeURIComponent($scope.subTitles));
            break;
    }
    $scope.startC = function() {
        $('.carousel').carousel({
            interval: 3000
        });
    };
    $scope.scrollTop = function () {
    	$('html, body').animate({
            scrollTop: 0
        }, 500);
    };
    $scope.showtitle = function (event) {
    	$(event.target).tooltip('toggle');
    };
    
});

app.controller('catalogCtrl1', function($scope, $rootScope, $location, $state, $window, $stateParams, $http, $timeout, demoCatalogService, CONSTANTS) {
	//$rootScope.$broadcast(CONSTANTS.appConfig.showLoader, false);
    //console.log($stateParams.catalog);
	$('#preloader').show();
	$window.scrollTo(0, 0);
    $scope.catalogTitle = $stateParams.catalog1;
    $scope.getUpdates = function (dat) {
		$window.scrollTo(0, 0);
		$rootScope.$broadcast(CONSTANTS.appConfig.showLoader, true);
		$('#preloader').show();
		demoCatalogService.getAppDetailsBasedOnGroup(encodeURIComponent(dat)).then(function(data) {
			if (data.status === CONSTANTS.responseStatusCodes.SUCCESS) {
				console.log(data.response.appDetails);
				$scope.homeContents = '';
				$scope.homeContents = data.response.appDetails;
				$scope.otherItems = [];
				//$rootScope.$broadcast(CONSTANTS.appConfig.showLoader, false);
				setTimeout(function () {
					$("a").tooltip();
					$('.tech-page').hide();
					$('.landing-page').show();
					$('.carousel').carousel({
			            interval: 3000
			        });
					$rootScope.$broadcast(CONSTANTS.appConfig.showLoader, false);
					$('#preloader').hide()
				}, 3000);
			} else {
				$rootScope.$broadcast(CONSTANTS.appConfig.showLoader, false);
				$('#preloader').hide()
			}
		});
	};
	
	$scope.techShow = function (dat) {
		//console.log(dat);
		//console.log($scope.homeContents1);
		$window.scrollTo(0, 0);
		$rootScope.$broadcast(CONSTANTS.appConfig.showLoader, true);
		$('#preloader').show();
		
		demoCatalogService.getLandingPageDetails().then(function(data) {
			if (data.status === CONSTANTS.responseStatusCodes.SUCCESS) {
				$scope.carouselContents = data.response.slider;
			}
		});
		$scope.homeContents = [];
		demoCatalogService.getAllDataDetails().then(function(data) {
			$scope.homeContents2 = [];
			if (data.status === CONSTANTS.responseStatusCodes.SUCCESS) {
				$scope.homeContents2 = data.response.appDetails;
				$.each($scope.homeContents2, function (j, temps) {
					if(temps.technologyArea){
					var list = temps.technologyArea;
					console.log(list);
					var newTemps = [];
					if(list.indexOf(', ') != -1){
						newTemps = temps.technologyArea.split(', ');
						$.each(newTemps, function (l, tmps) {
							if(tmps == dat){
								$scope.homeContents.push($scope.homeContents2[j]);
							}
						});
					}
					
					if(temps.technologyArea == dat){
						$scope.homeContents.push($scope.homeContents2[j]);
					}
					}
				});
				setTimeout(function () {
					$("a").tooltip();
					$('.tech-page').show();
					$('.landing-page').hide();
					$rootScope.$broadcast(CONSTANTS.appConfig.showLoader, false);
					$('#preloader').hide();
				}, 3000);
			}else{
				$('.tech-page').hide();
				$rootScope.$broadcast(CONSTANTS.appConfig.showLoader, false);
				$('#preloader').hide();
			}
		});
	}
	$scope.techShow($scope.catalogTitle);
    //$scope.startC();
});

$(function() {
    $("#ops-menu").on("click", function(event) {
        event.stopPropagation();
        $(".sub-drops").show();
    });
    $(".sub-drops li a").on("click", function(e) {
        $(".sub-drops").hide();
    });
    $('html').click(function() {
        $(".sub-drops").hide();
    });
    $('.home-menu, .ops-menu, .scroll-menus').hide();
});