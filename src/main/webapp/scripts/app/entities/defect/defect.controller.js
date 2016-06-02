'use strict';

angular.module('lighthouseApp')
    .controller('DefectController', function ($scope, $state, $stateParams, Defect, DefectSearch, ParseLinks, customFilterFilter, $q, $httpParamSerializer) {
        $scope.predicate = 'severity';
        $scope.reverse = false;
        $scope.page = 1;
        $scope.defects = [];
        $scope.searchDTO = {};

        $scope.loadAllDefects = function() {
            Defect.query({
                scanId: $stateParams.scanId,
                filterParams: $httpParamSerializer($scope.searchDTO),
                page: $scope.page - 1,
                size: 10,
                sort: [$scope.predicate + ',' + ($scope.reverse ? 'asc' : 'desc'), 'id']
            }, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.totalItems = headers('X-Total-Count');
                $scope.defects = result;
            });
        };

        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAllDefects();
        };
        //$scope.loadAllDefects();

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
        };

        $scope.switchNeedManuelCheckFlag = function(defect, needManuelCheck){
            defect.needManuelCheck = needManuelCheck;
            Defect.update(defect);
        };


        //$scope.searchDefects = function (predicateObject) {
        //    var deferred = $q.defer();
        //    DefectSearch.query({
        //        page: $scope.page - 1,
        //        size: $scope.size,
        //        sort: $scope.sort,
        //        search: predicateObject,
        //        scanId: $stateParams.scanId
        //    }, function (result, headers) {
        //        $scope.links = ParseLinks.parse(headers('link'));
        //        $scope.totalItems = headers('X-Total-Count');
        //        deferred.resolve(result);
        //    }, function () {
        //        deferred.reject();
        //    });
        //
        //    deferred.promise.then(function (result) {
        //        $scope.defects = result;
        //    })
        //};

        //$scope.callServer = function(tableState) {
        //    $scope.isLoading = true;
        //    searchDefects(tableState)
        //        .then(function(response) {
        //            $scope.isLoading = false;
        //            $scope.defects = response;
        //            $scope.displayedDefects = response;
        //            tableState.pagination.numberOfPages = Math.ceil(response.total / tableState.pagination.number);
        //        });
        //};
        //
        //function searchDefects(tableState){
        //    var deferred = $q.defer();
        //    DefectSearch.query({
        //        page: tableState.pagination.start,
        //        size: 10,
        //        sort: tableState.sort,
        //        search: tableState.search,
        //        scanId: $stateParams.scanId
        //    }, function(result, headers){
        //        $scope.links = ParseLinks.parse(headers('link'));
        //        $scope.totalItems = headers('X-Total-Count');
        //        deferred.resolve(result);
        //    }, function(){
        //        deferred.reject($scope.loadAllDefects());
        //    });
        //
        //    return deferred.promise;
        //}

        //$scope.$watch("defectSearchDTO", function(searchDTO){
        //    if(searchDTO == null){
        //        return;
        //    }
        //    searchDTO.page = $scope.page - 1;
        //    searchDTO.size = 10;
        //    searchDTO.sort = $scope.predicate; // + ',' + ($scope.reverse ? 'asc' : 'desc')
        //    //searchDTO.specifications = ['title:Nessus'];
        //    searchDTO.scanId = $stateParams.scanId;
        //    searchDTO.specifications = $scope.searchDTO.specifications;
        //    //alert(JSON.stringify(searchDTO.specifications));
        //    DefectSearch.query(searchDTO, function(result) {
        //        $scope.defects = result;
        //    }, function(response) {
        //        if(response.status === 404) {
        //            $scope.loadAllDefects();
        //        }
        //    });
        //});
        //
        //$scope.addSearchCriteria = function(event){
        //    //alert(searchCriteria);
        //    if($scope.searchDTO.specifications == null){
        //        $scope.searchDTO.specifications = [];
        //    }
        //    var searchCriteria = event.currentTarget.getAttribute('st-search') + ':' + event.currentTarget.getAttribute('value');
        //    $scope.searchDTO.specifications.push(searchCriteria);
        //    //alert(JSON.stringify(searchCriteria));
        //}


    })
