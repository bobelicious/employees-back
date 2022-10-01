package com.augusto.employees.repository;

import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

import com.augusto.employees.model.ClockIn;
import com.augusto.employees.model.Employees;
import com.augusto.employees.model.EntryOrLeft;

public interface ClockInRespository extends JpaRepository<ClockIn,Long> {
    public Set<ClockIn> findByEmployees(Employees employee);
    public Optional<ClockIn> findByName(EntryOrLeft entryOrLeft);
}
