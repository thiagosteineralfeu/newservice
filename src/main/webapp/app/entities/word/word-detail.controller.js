(function() {
    'use strict';

    angular
        .module('myserviceApp')
        .controller('WordDetailController', WordDetailController);

    WordDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Word', 'WordOccurrences', 'WordRank'];

    function WordDetailController($scope, $rootScope, $stateParams, previousState, entity, Word, WordOccurrences, WordRank) {
        var vm = this;

        vm.word = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('myserviceApp:wordUpdate', function(event, result) {
            vm.word = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
