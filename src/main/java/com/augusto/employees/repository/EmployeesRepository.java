package com.augusto.employees.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

import com.augusto.employees.model.Employees;

public interface EmployeesRepository extends JpaRepository<Employees,Long> {
    public Optional<Employees> findByEmail(String email);
    public Optional<Employees> findByCpf(String cpf);
    public Optional<Employees> findByCpfOrEmail(String cpf, String email);
    public Optional<Employees> findByCpfContainingIgnoreCaseOrEmailContainingIgnoreCase(String cpf, String email);
}
