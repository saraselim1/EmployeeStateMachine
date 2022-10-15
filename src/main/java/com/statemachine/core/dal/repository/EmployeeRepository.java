package com.statemachine.core.dal.repository;

import com.statemachine.core.dal.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee,Long> {

    boolean existsByEmail(String mail);
}
