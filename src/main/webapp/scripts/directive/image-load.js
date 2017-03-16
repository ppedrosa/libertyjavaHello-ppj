app.directive('imageOnLoad', function() {
    return {
        restrict: 'A',
        link: function(scope, element, attrs) {
            element.bind('load', function() {
                	$(".scroll-menus").hide();
                	$(".scroll-menus1, .scroll-menus1 div").show();
                    $(".scroll-menus1").css("top", $(".carousel-inner").height() + 73 + "px").show();
            });
        }
    };
});