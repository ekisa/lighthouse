/**
 * Created by 010235 on 11.05.2016.
 */
'use strict';

angular.module('lighthouseApp')
    .directive('myTableSearchText', function(DefectSearch) {
        return {
            restrict: 'E',
            transclude: true,
            replace: true,
            scope:{
                mySearch : '@',
                myTranslate : '@',
                mySearchDTO : '='
            },
            require: '^stTable',
            template:
                          '<span translate="{{myTranslate}}">text</span>' +
                          '<input placeholder="Search" st-search="{{mySearch}}" />' +
                          '<span class="glyphicon glyphicon-sort"></span>',

            controller: ['$scope',
                function($scope) {


                    this.addSearchCriteria = function (key, operation, value) {
                        if (operation === null){
                            operation = ":";
                        }

                        if($scope.mySearchDTO.specifications == null){
                            $scope.mySearchDTO.specifications = [];
                        }
                        var searchCriteria = key + operation + value;
                        alert(searchCriteria);
                        $scope.mySearchDTO.specifications.push(searchCriteria);

                        $scope.$apply();
                        $scope.callback();



                    }

                    //$scope.alerts = AlertService.get();
                    $scope.$on('$destroy', function () {
                        //$scope.alerts = [];
                    });
                }
            ],
            link: function(scope, element, attrs, tableCtrl){
                $scope.watch("searchDTO", function())
            }
        }
    })
    .directive('myTableSearchTextListener', function () {
        return {
            restrict: 'A',
            scope: false,
            require: '^myTableSearchText',
            link: function (scope, element, attrs, parentCtrl) {
                element.bind('keydown', function () {
                    parentCtrl.addSearchCriteria(attrs.myTableSearchTextListener, null, element.val());
                    alert(attrs.myTableSearchTextListener);
                });
            }
        };
    })
    .filter('customFilter', ['$filter', function($filter) {
        var filterFilter = $filter('filter');
        var standardComparator = function standardComparator(obj, text) {
            text = ('' + text).toLowerCase();
            return ('' + obj).toLowerCase().indexOf(text) > -1;
        };

        return function customFilter(array, expression) {
            function customComparator(actual, expected) {
                var isBeforeActivated = expected.before;
                var isAfterActivated = expected.after;
                var isLower = expected.lower;
                var isHigher = expected.higher;
                var higherLimit;
                var lowerLimit;
                var itemDate;
                var queryDate;

                if (angular.isObject(expected)) {
                    //exact match
                    if (expected.distinct) {
                        if (!actual || actual.toLowerCase() !== expected.distinct.toLowerCase()) {
                            return false;
                        }

                        return true;
                    }

                    //matchAny
                    if (expected.matchAny) {
                        if (expected.matchAny.all) {
                            return true;
                        }

                        if (!actual) {
                            return false;
                        }

                        for (var i = 0; i < expected.matchAny.items.length; i++) {
                            if (actual.toLowerCase() === expected.matchAny.items[i].toLowerCase()) {
                                return true;
                            }
                        }

                        return false;
                    }

                    //date range
                    if (expected.before || expected.after) {
                        try {
                            if (isBeforeActivated) {
                                higherLimit = expected.before;

                                itemDate = new Date(actual);
                                queryDate = new Date(higherLimit);

                                if (itemDate > queryDate) {
                                    return false;
                                }
                            }

                            if (isAfterActivated) {
                                lowerLimit = expected.after;


                                itemDate = new Date(actual);
                                queryDate = new Date(lowerLimit);

                                if (itemDate < queryDate) {
                                    return false;
                                }
                            }

                            return true;
                        } catch (e) {
                            return false;
                        }

                    } else if (isLower || isHigher) {
                        //number range
                        if (isLower) {
                            higherLimit = expected.lower;

                            if (actual > higherLimit) {
                                return false;
                            }
                        }

                        if (isHigher) {
                            lowerLimit = expected.higher;
                            if (actual < lowerLimit) {
                                return false;
                            }
                        }

                        return true;
                    }
                    //etc

                    return true;

                }
                return standardComparator(actual, expected);
            }

            var output = filterFilter(array, expression, customComparator);
            return output;
        };
    }])
