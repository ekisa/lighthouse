/**
 * Created by 010235 on 11.05.2016.
 */
'use strict';

angular.module('lighthouseApp')
    .directive('mySearchForm', function(DefectSearch, ParseLinks, $stateParams, $q) {
        return {
            restrict: 'A',
            transclude: false,
            replace: false,
            scope:{
                myCollection : '=',
                mySearchDto : '=',
                page : '=',
                size : '=',
                sort : '=',
                searchMethod : '&'
            },
            controller: ['$scope',
                function($scope) {
                    this.addSearchCriteria = function (searchField, searchValue) {
                        $scope.mySearchDto[searchField] = searchValue;
                    };

                    $scope.$watch("mySearchDto", function(predicateObject){
                        //console.log("mySearchDTO : " + JSON.stringify(predicateObject));
                        if(!angular.equals(predicateObject, {})){
                            //$scope.search({predicateObject : predicateObject});
                            $scope.searchMethod();
                        }
                    }, true);

                    $scope.$on('$destroy', function () {
                        $scope.myCollection = [];
                        $scope.mySearchDto = null;
                        $scope.page = null;
                        $scope.size = null;
                        $scope.sort = null;
                        $scope.searchMethod = null;
                    });
                }
            ],
            link: function(scope, element, attrs, ctrl){
                scope.searchMethod();

                //alert(JSON.stringify(element));
                //alert(JSON.stringify(attrs));

                //scope.searchMethod(scope.mySearchDto);
                //console.log("COLLECTION 1 : " + JSON.stringify(scope.myCollection));
                //scope.$watch("myCollection", function(collectionData){
                //    //console.log("element : " + JSON.stringify(element));
                //    //console.log("attrs : " + JSON.stringify(attrs));
                //    //console.log("ctrl : " + JSON.stringify(ctrl));
                //    //console.log("COLLECTION 2 : " + JSON.stringify(collectionData));
                //    ctrl.updateCollection(collectionData);
                //
                //}, true);
            }
        }
    })
    .directive('mySearchFormField', function() {
        return {
            restrict: 'E',
            //replace: false,
            require: '^^mySearchForm',
            transclude: false,
            scope: {
                mySearch: '@'
            },
            template:
                    '<input placeholder="Filter.." ng-model="searchValue" ng-change="search(searchValue)" title="{{\'global.menu.search.tip\' | translate}}"/>',


            controller: ['$scope',
                function ($scope) {
                    $scope.search = function search(searchValue){
                        $scope.searchControl.addSearchCriteria($scope.mySearch, searchValue);
                    };

                    $scope.$on('$destroy', function () {
                        $scope.mySearch = null;
                    });
                }
            ],
            link: function (scope, element, attrs, searchControl) {
                scope.searchControl = searchControl;
                //element.bind('keydown', function () {
                //
                //    console.log('PARENT CTRL: ' + JSON.stringify(searchControl));
                ////
                ////    //parentCtrl.addSearchCriteria({searchValue: scope.searchValue});
                ////    parentCtrl.addSearchCriteria(scope.searchValue);
                //});

            }
        };
})
.directive('mySearchFormSelectOneField', function() {
    return {
        restrict: 'E',
        //replace: false,
        require: '^^mySearchForm',
        transclude: false,
        scope: {
            mySearch: '@'
        },
        template:
        '<select ng-model="searchValue" ng-options="selectionOption as selectionOption for selectionOption in selectionOptions" ng-change="search(searchValue)">' +
            '<option value="">-- Filter --</option>' +
        '</select>',
        controller: ['$scope',
            function ($scope) {
                $scope.search = function search(searchValue){
                    $scope.searchControl.addSearchCriteria($scope.mySearch, searchValue);
                };

                $scope.$on('$destroy', function () {
                    $scope.mySearch = null;
                });
            }
        ],
        link: function (scope, element, attrs, searchControl) {
            scope.searchControl = searchControl;
            //Array yaratıyoruz, yoksa string olarak görüyor
            scope.selectionOptions = scope.$eval(attrs.selectionOptions);
        }
    };
})
.directive('mySearchKeyListener', function () {
    return {
        restrict: 'A',
        scope: false,
        require: '^mySearchFormField',
        link: function (scope, element, attrs, parentCtrl) {
            element.bind('keydown', function () {

            });
        }
    };
});
