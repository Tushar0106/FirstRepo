var app = angular.module('myApp', []);

/* Angular Practice code*/
app.controller('myCtrl', function($scope) {
	$scope.products = ["Milk", "Bread", "Cheese"];
	$scope.addItem = function() {
		if ($scope.addMe) {
			$scope.products.push($scope.addMe);
			$scope.addMe = ""; // Clear the input field
		}
	};
	$scope.removeItem = function(x) {
		$scope.products.splice(x, 1);
	}
});

/* Insert new employee */
app.controller('formController', ['$scope', '$http', '$window', function($scope, $http, $window) {
	$scope.employee = {}; // Initialize employee object to bind form data

	$scope.submitForm = function() {
		// Send a POST request to the server with employee data
		$http.post('/addemployee', $scope.employee);
		$window.location.href = '/table';
	};
}]);

/* Employee list - table */
app.controller('empListController', function($scope, $http, $window) {
	$scope.employees = [];

	$scope.redirectToUpdate = function(empID) {
		console.log("Employee ID: ",empID);
		$window.location.href = '/updateemp.html?empID=' + empID;
	};

	$scope.deleteEmp = function(empID) {
        if (confirm("Are you sure you want to delete this employee?")) {
            $http.delete('/deleteEmp/' + empID)
                .then(function() {                                
                    $scope.loadEmployees();
                });
        }
    };
    
    $scope.loadEmployees = function() {
        $http.get('/emplist')
            .then(function(response) {
                $scope.employees = response.data;
            })
            .catch(function(error) {
                console.error("Error loading employees:", error);
            });
    };

    // Fetch data from Spring Boot API
    $scope.loadEmployees();
});

/* Update employee */
app.controller('updateController', ['$scope', '$http', '$window', function($scope, $http, $window) {
	$scope.employee = {};

	// Extract empID from the URL query string
	const urlParams = new URLSearchParams($window.location.search);
	const empID = urlParams.get('empID');

	if (empID) {
		console.log("Employee ID:", empID);
		$http.get('/showedit/' + empID).then(function(response) {
			$scope.employee = response.data;
		}, function(error) {
			alert('Failed to load employee data.', error);

		});
	}

	//Initial password invisible
	$scope.passwordVisible = false;
	// Toggle password visibility
	$scope.togglePasswordVisibility = function() {
		$scope.passwordVisible = !$scope.passwordVisible;
	};

	// Update employee logic
	$scope.updateEmployee = function() {
		$http.put('/updateemp', $scope.employee);
		$window.location.href = '/table';
	};
}]);




