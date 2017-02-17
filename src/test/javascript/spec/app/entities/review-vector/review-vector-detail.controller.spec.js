'use strict';

describe('Controller Tests', function() {

    describe('ReviewVector Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockReviewVector, MockReview, MockRankSnapshot;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockReviewVector = jasmine.createSpy('MockReviewVector');
            MockReview = jasmine.createSpy('MockReview');
            MockRankSnapshot = jasmine.createSpy('MockRankSnapshot');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'ReviewVector': MockReviewVector,
                'Review': MockReview,
                'RankSnapshot': MockRankSnapshot
            };
            createController = function() {
                $injector.get('$controller')("ReviewVectorDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'myserviceApp:reviewVectorUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
