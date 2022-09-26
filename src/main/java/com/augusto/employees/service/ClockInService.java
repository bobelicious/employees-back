package com.augusto.employees.service;

import java.time.LocalDateTime;
import java.util.Set;

import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.augusto.employees.model.Employees;
import com.augusto.employees.model.EntryOrLeft;
import com.augusto.employees.model.ClockIn;
import com.augusto.employees.repository.EmployeesRepository;
import com.augusto.employees.repository.ClockInRespository;

@Service
public class ClockInService {

    @Autowired
    private ClockInRespository clockInRespository;
    @Autowired
    private EmployeesRepository employeesRepository;

    public ClockIn registerEntry() {
        var point = new ClockIn();
        point.setEmployees(getEmployee());
        point.setTime(LocalDateTime.now());
        point.setName(EntryOrLeft.ENTRADA);
        return clockInRespository.save(point);
    }

    public ClockIn registerLeft() {
        var point = new ClockIn();
        point.setEmployees(getEmployee());
        point.setTime(LocalDateTime.now());
        point.setName(EntryOrLeft.SAIDA);
        return clockInRespository.save(point);
    }

    public ClockIn editRegister(Long id, LocalDateTime time) {
        var point = clockInRespository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Registro de ponto não encontrado"));
        point.setTime(time);
       return clockInRespository.save(point);
    }

    public Set<ClockIn> getRegistry(Long id){
        var employee = findEmployee(id);
        return clockInRespository.findByEmployees(employee);
    }

    private Employees findEmployee(Long id){
        var employee = employeesRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Funcionário não encontrado"));
        return employee;
    }

    public Employees getEmployee() {
        var principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var name = ((UserDetails) principal).getUsername();
        var employee = employeesRepository.findByCpfOrEmail(name, name);
        return employee.get();
    }
}
