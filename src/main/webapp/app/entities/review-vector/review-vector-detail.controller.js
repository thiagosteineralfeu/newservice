(function() {
    'use strict';

    angular
        .module('myserviceApp')
        .controller('ReviewVectorDetailController', ReviewVectorDetailController);

    ReviewVectorDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'ReviewVector', 'Review', 'RankSnapshot'];

    function ReviewVectorDetailController($scope, $rootScope, $stateParams, previousState, entity, ReviewVector, Review, RankSnapshot) {
        var vm = this;

        vm.reviewVector = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('myserviceApp:reviewVectorUpdate', function(event, result) {
            vm.reviewVector = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
