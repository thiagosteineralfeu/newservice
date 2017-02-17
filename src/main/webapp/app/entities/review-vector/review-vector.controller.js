(function() {
    'use strict';

    angular
        .module('myserviceApp')
        .controller('ReviewVectorController', ReviewVectorController);

    ReviewVectorController.$inject = ['ReviewVector'];

    function ReviewVectorController(ReviewVector) {
        var vm = this;

        vm.reviewVectors = [];

        loadAll();

        function loadAll() {
            ReviewVector.query(function(result) {
                vm.reviewVectors = result;
                vm.searchQuery = null;
            });
        }
    }
})();
