package com.testangular.services;

import com.testangular.models.Employee;
import com.testangular.repository.EmployeeRepo;

import jakarta.persistence.EntityNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServices {
    @Autowired
    EmployeeRepo empRepo;

    public List<Employee> empList() {
    	List<Employee> empList = new ArrayList<Employee>();
    	empRepo.findAll().forEach(empList::add);
    	return empList;
    }
    
    public Optional<Employee> getEmpById(Integer empid){
    		return empRepo.findById(empid);
    }
    
    public Employee saveAndUpdateEmployee(Employee employee) {
    	return empRepo.save(employee);
    }
    
    public void deleteEmpById(Integer empID) {
    	Employee employee = empRepo.findById(empID).orElseThrow(() -> new EntityNotFoundException("Entity not found with id: " + empID));
    	empRepo.deleteById(empID);
    }
}
