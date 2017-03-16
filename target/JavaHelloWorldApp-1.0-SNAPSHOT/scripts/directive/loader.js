'use strict';


app.directive('loader', ['$rootScope', function($rootScope) {
    return {
        restrict: 'E',
        templateUrl: 'views/loader.html',
        link: function(scope) {
            scope.showLoader = false;
            scope.$on("show_loader", function( events, param ) {
                scope.showLoader = param;
                try {
                    $rootScope.safeApply();
                } catch (exception) {
                }
            });
        }
    };
}]);
