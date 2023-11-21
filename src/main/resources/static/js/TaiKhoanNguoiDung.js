var app = angular.module('nguoidung-admin', []);
app.controller('ctrl', function ($scope, $http) {

    $scope.khachHang = [];
    $scope.khachHangDetail = {};
    $scope.totalPage = 0;
    $scope.pageNumbers = [];
    $scope.pageNumber = 0;


    $scope.getAll = function (page){
        $scope.pageNumber = page
        $http.get("/admin/khach-hang/get-all-khach-hang?page="+page).then(r =>{
            $scope.khachHang = r.data.content;
            $scope.totalPage = r.data.totalPages;
            $scope.getPageNumbers(r.data.totalPages)
        }).catch(e => console.log(e))
    }
    $scope.getAll(0)
    $scope.getPageNumbers = function (totalPages){
        $scope.pageNumbers = [];
        for (let i = 0; i< totalPages;i++){
            $scope.pageNumbers.push(i);
        }
    }

    $scope.detail = function (id){
        $http.get("/admin/khach-hang/detail/"+id).then(r => {
            $scope.khachHangDetail = r.data;
            console.log($scope.khachHangDetail)
        }).catch(e => console.log(e))
    }
})