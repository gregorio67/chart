<%@ page language="java" contentType="text/html; charset=EUC-KR" pageEncoding="EUC-KR"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
<link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">
<script src="/resources/js/angular/angular.1.6.4.min.js"></script>
<style>
	table, th , td  {
	  border: 1px solid grey;
	  border-collapse: collapse;
	  padding: 5px;
	}
	table tr:nth-child(odd) {
	  background-color: #f1f1f1;
	}
	table tr:nth-child(even) {
	  background-color: #ffffff;
	}
</style>

<title>Insert title here</title>
</head>
<body>

	<div ng-app="myApp" ng-controller="customersCtrl"> 
		<fieldset ng-view>
			<label for="name">name:</label>
			<input type="text" ng-model="name" />
			<button ng-click="callApp()">조회</button>
		</fieldset>
	
		<table>
		  <tr ng-repeat="x in names">
		    <td>{{ x.Name }}</td>
		    <td>{{ x.Country }}</td>
		  </tr>
		</table>

		<script>
			var app = angular.module('myApp', []);
			debugger;
			//app.controller('customersCtrl', function($scope, $http) {
			//	var param = JSON.stringify({'name': $scope.name});
			//	$scope.callApp = function() {
			//		debugger;
			//	    $http.jsonp("country.do")
			//	    .then(function (response) {$scope.names = response.data;});	
			//	}
			//});
		
			app.controller('customersCtrl', function($scope, $http) {					
				$scope.callApp = function() {
					var data = {'name':$scope.name,
								'age': '10'};
					
					var req = {
							method: 'POST',
							url: 'country.do',
							headers: {'Content-Type' : 'application/json'},
							data: data
					}
					debugger;
					$http(req).then(function (response) {$scope.names = response.data;},
									function(response) {alert(response.status)});
				}
			});

		</script>
	</div>

</body>
</html>
