package com.testangular.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.testangular.models.Employee;
import com.testangular.services.EmployeeServices;

@Controller
//@RestController
//@RequestMapping("/api")  // Correct base URL mapping for the controller
public class EmployeeController {

	private static final Logger logger = LoggerFactory.getLogger(EmployeeController.class);
	private static final String UPLOAD_DIR = "C:/SpringBoot/TestAngular/src/main/resources/static/uploads/";
	
    @Autowired
    EmployeeServices empService;

    @RequestMapping("/")
    public String goToIndex() {
        return "index.html";
    }

    @RequestMapping("/angular")
    public String goToAngular() {
        return "angularPractice.html";
    }

    @RequestMapping("/table")
    public String goToTable() {
        return "table.html";
    }

    /*	List all employees	*/ 
	@GetMapping("/emplist")
	public ResponseEntity<List<Employee>> empList() {
        List<Employee> items = fetchReq();
        System.out.println(items);
        return new ResponseEntity<>(items, HttpStatus.OK);
    }
	public List<Employee> fetchReq() {
        return empService.empList();
    }

    @RequestMapping("/addEmp")
    public String goToAddEmp() {
        return "addEmployee.html";
    }

	/* Add new employee with file */
    @PostMapping("/upload")
    public ResponseEntity<Map<String, String>> uploadProfileImage(
            @RequestParam("file") MultipartFile file,
            @RequestParam("empname") String empname,
            @RequestParam("address") String address,
            @RequestParam("email") String email,
            @RequestParam("salary") String salary) {

        Map<String, String> response = new HashMap<>();
        try {
            // Ensure upload directory exists
            File uploadDir = new File(UPLOAD_DIR);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
                System.out.println("New folder created!!"+uploadDir.getAbsolutePath());
            }

            // Generate file path
            String originalFileName = file.getOriginalFilename();
            String filePath = UPLOAD_DIR + originalFileName;

            // Save file locally
            file.transferTo(new File(filePath));

            // Save employee data
            Employee employee = new Employee();
            employee.setEmpname(empname);
            employee.setAddress(address);
            employee.setEmail(email);
            employee.setSalary(salary);
            employee.setFilename(originalFileName);

            empService.saveAndUpdateEmployee(employee);

            response.put("message", "File uploaded successfully");
            response.put("filename", originalFileName);
            return ResponseEntity.ok(response);

        } catch (IOException e) {
            response.put("error", "File upload failed");
            logger.error("Unable to upload file "+e);
            return ResponseEntity.status(500).body(response);
        }
    }
    
    /* Add new employee */
    @PostMapping("/addemployee")
    public ResponseEntity<String> insertData(@RequestBody Employee emp) {
    	try {
            empService.saveAndUpdateEmployee(emp);
            return ResponseEntity.status(HttpStatus.CREATED).body("Employee added successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to add employee: " + e.getMessage());
        }
    }

	/* Post list of records */
//    @PostMapping("/addemployee")
//    public ResponseEntity<String> insertData(@RequestBody List<Employee> employees) {
//        try {
//            for (Employee emp : employees) {
//                empService.saveAndUpdateEmployee(emp);
//            }
//            return ResponseEntity.status(HttpStatus.CREATED).body("Employees added successfully!");
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to add employees: " + e.getMessage());
//        }
//    }
    
    /*Show employee by ID*/ 
	@RequestMapping("/showedit")
	public String goToEdit() {
		return "updateemp.html";
	}

	@GetMapping("/showedit/{empID}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable("empID") Integer empID) {
        Optional<Employee> emp = empService.getEmpById(empID);
        if (emp.isPresent()) {
            return new ResponseEntity<>(emp.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

	@PutMapping("/updateemp")
	public ResponseEntity<?> updateEmp(@RequestBody Employee employee) {
		try {
			Employee updatedEmp = empService.saveAndUpdateEmployee(employee);
			return new ResponseEntity<>(updatedEmp, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>("Failed to update employee: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}	
	}
	
	/* Add new employee with file */
	@PutMapping("/update")
    public ResponseEntity<Map<String, String>> updateProfileImage(
            @RequestParam("file") MultipartFile file,
            @RequestParam("empname") String empname,
            @RequestParam("address") String address,
            @RequestParam("email") String email,
            @RequestParam("salary") String salary) {

        Map<String, String> response = new HashMap<>();
        try {
            // Save employee data
            Employee employee = new Employee();
            employee.setEmpname(empname);
            employee.setAddress(address);
            employee.setEmail(email);
            employee.setSalary(salary);
            //employee.setFilename(originalFilename);

            if (file != null && !file.isEmpty()) {
                String originalFilename = file.getOriginalFilename();
                String filePath = UPLOAD_DIR + originalFilename;
                file.transferTo(new File(filePath));
                employee.setFilename(originalFilename); // Update filename
            }
            
            empService.saveAndUpdateEmployee(employee);

            response.put("message", "File uploaded successfully");
            response.put("filename", employee.getFilename());
            return ResponseEntity.ok(response);

        } catch (IOException e) {
            response.put("error", "File upload failed");
            logger.error("Unable to upload file "+e);
            return ResponseEntity.status(500).body(response);
        }
    }
	
	@DeleteMapping("/deleteEmp/{empID}")
	public ResponseEntity<?> deleteEmp(@PathVariable Integer empID) {
		empService.deleteEmpById(empID);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
}
