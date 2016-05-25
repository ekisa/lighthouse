/**
 * Created by 010235 on 11.05.2016.
 */
'use strict';

angular.module('lighthouseApp')
    .directive('mySearchForm', function(DefectSearch, ParseLinks, $stateParams, $q) {
        return {
            restrict: 'E',
            transclude: false,
            //replace: false,
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
                            $scope.searchMethod({predicateObject : predicateObject});
                        }
                    }, true);

                    $scope.$on('$destroy', function () {
                        $scope.myCollection = [];
                        $scope.mySearchDto = null;
                    });
                }
            ],
            link: function(scope, element, attrs, ctrl){
                //alert(JSON.stringify(element));
                //alert(JSON.stringify(attrs));

                //scope.$watch("mySearchDto", function(predicateObject){
                //    if(!angular.equals(predicateObject, {})){
                //        searchForm.search(predicateObject);
                //    }
                //}, true)
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
                mySearch: '@',
                myLabel: '@'
            },
            template:
                    '<span translate="{{myLabel}}">text</span>' +
                    '<span class="glyphicon glyphicon-sort"></span>' +
                    '<input placeholder="Search.." ng-model="searchValue" ng-change="search(searchValue)"/>',


            controller: ['$scope',
                function ($scope) {
                    $scope.search = function search(searchValue){
                        $scope.searchControl.addSearchCriteria($scope.mySearch, searchValue);
                    };

                    $scope.$on('$destroy', function () {
                        $scope.mySearch = null;
                        $scope.myLabel = null;
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
