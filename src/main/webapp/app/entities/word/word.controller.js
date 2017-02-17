(function() {
    'use strict';

    angular
        .module('myserviceApp')
        .controller('WordController', WordController);

    WordController.$inject = ['$scope', '$state', 'Word'];

    function WordController ($scope, $state, Word) {
        var vm = this;

        vm.words = [];

        loadAll();

        function loadAll() {
            Word.query(function(result) {
                vm.words = result;
                vm.searchQuery = null;
            });
        }
    }
})();
