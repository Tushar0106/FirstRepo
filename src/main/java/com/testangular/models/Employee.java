package com.testangular.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


@Entity
@Table(name = "employee")
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "empID")
    private int empID;

    @Column(name = "empname")
    private String empname;

    @Column(name = "address")
    private String address;

    @Column(name = "email")
    private String email;

    @Column(name = "salary")
    private String salary;

    public Employee() {
    }

    public Employee(String empname, int empID, String address, String email, String salary) {
        this.empname = empname;
        this.empID = empID;
        this.address = address;
        this.email = email;
        this.salary = salary;
    }
    
    public int getEmpID() {
		return empID;
	}

	public void setEmpID(int empID) {
		this.empID = empID;
	}

	public String getEmpname() {
		return empname;
	}

	public void setEmpname(String empname) {
		this.empname = empname;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSalary() {
		return salary;
	}

	public void setSalary(String salary) {
		this.salary = salary;
	}

	@Override
    public String toString() {
        return "Employee{" +
                "empID=" + empID +
                ", empname='" + empname + '\'' +
                ", address='" + address + '\'' +
                ", email='" + email + '\'' +
                ", salary='" + salary + '\'' +
                '}';
    }
}
