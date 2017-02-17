(function() {
    'use strict';

    angular
        .module('myserviceApp')
        .controller('ReviewDialogController', ReviewDialogController);

    ReviewDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'DataUtils', 'entity', 'Review', 'WordOccurrences', 'Book', 'ReviewVector'];

    function ReviewDialogController ($timeout, $scope, $stateParams, $uibModalInstance, DataUtils, entity, Review, WordOccurrences, Book, ReviewVector) {
        var vm = this;

        vm.review = entity;
        vm.clear = clear;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
        vm.save = save;
        vm.wordoccurrences = WordOccurrences.query();
        vm.books = Book.query();
        vm.reviewvectors = ReviewVector.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.review.id !== null) {
                Review.update(vm.review, onSaveSuccess, onSaveError);
            } else {
                Review.save(vm.review, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('myserviceApp:reviewUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
