(function() {
    'use strict';

    angular
        .module('myserviceApp')
        .controller('WordOccurrencesDetailController', WordOccurrencesDetailController);

    WordOccurrencesDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'WordOccurrences', 'Word', 'Review'];

    function WordOccurrencesDetailController($scope, $rootScope, $stateParams, previousState, entity, WordOccurrences, Word, Review) {
        var vm = this;

        vm.wordOccurrences = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('myserviceApp:wordOccurrencesUpdate', function(event, result) {
            vm.wordOccurrences = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
