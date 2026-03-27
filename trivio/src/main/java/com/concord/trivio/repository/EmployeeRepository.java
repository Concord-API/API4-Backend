package com.concord.trivio.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.concord.trivio.entity.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

}
