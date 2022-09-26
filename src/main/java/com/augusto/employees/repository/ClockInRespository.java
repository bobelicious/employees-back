package com.augusto.employees.repository;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

import com.augusto.employees.model.ClockIn;
import com.augusto.employees.model.Employees;

public interface ClockInRespository extends JpaRepository<ClockIn,Long> {
    public Set<ClockIn> findByEmployees(Employees employees);
}
