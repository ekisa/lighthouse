'use strict';

angular.module('lighthouseApp')
    .controller('DefectController', function ($scope, $state, $stateParams, Defect, DefectSearch, ParseLinks) {
        $scope.predicate = 'severity';
        $scope.reverse = true;
        $scope.page = 1;
        $scope.defects = [];
        $scope.searchDTO = {};

        $scope.loadAllDefects = function() {
            Defect.query({scanId: $stateParams.scanId, page: $scope.page - 1, size: 10, sort: [$scope.predicate + ',' + ($scope.reverse ? 'asc' : 'desc'), 'id']}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.totalItems = headers('X-Total-Count');
                $scope.defects = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAllDefects();
        };
        $scope.loadAllDefects();

        $scope.refresh = function () {
            $scope.loadAllDefects();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.defect = null;
        };

        $scope.switchFalsePositiveFlag = function(defect, falsePositive){
            defect.falsePositive = falsePositive;
            Defect.update(defect);
        }

        $scope.switchNeedManuelCheckFlag = function(defect, needManuelCheck){
            defect.needManuelCheck = needManuelCheck;
            Defect.update(defect);
        }

        $scope.$watch("defectSearchDTO", function(searchDTO){
            if(searchDTO == null){
                return;
            }
            searchDTO.page = $scope.page - 1;
            searchDTO.size = 10;
            searchDTO.sort = $scope.predicate; // + ',' + ($scope.reverse ? 'asc' : 'desc')
            //searchDTO.specifications = ['title:Nessus'];
            searchDTO.scanId = $stateParams.scanId;
            searchDTO.specifications = $scope.searchDTO.specifications;
            //alert(JSON.stringify(searchDTO.specifications));
            DefectSearch.query(searchDTO, function(result) {
                $scope.defects = result;
            }, function(response) {
                if(response.status === 404) {
                    $scope.loadAllDefects();
                }
            });
        });

        $scope.addSearchCriteria = function(event){
            //alert(searchCriteria);
            if($scope.searchDTO.specifications == null){
                $scope.searchDTO.specifications = [];
            }
            var searchCriteria = event.currentTarget.getAttribute('st-search') + ':' + event.currentTarget.getAttribute('value');
            $scope.searchDTO.specifications.push(searchCriteria);
            //alert(JSON.stringify(searchCriteria));
        }


    })
