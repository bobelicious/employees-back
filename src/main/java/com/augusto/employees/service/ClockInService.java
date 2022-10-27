package com.augusto.employees.service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.augusto.employees.exceptions.EmployeeException;
import com.augusto.employees.exceptions.ResourceNotFoundException;
import com.augusto.employees.model.ClockIn;
import com.augusto.employees.model.Employees;
import com.augusto.employees.model.EntryOrLeft;
import com.augusto.employees.payload.ClockInDto;
import com.augusto.employees.repository.ClockInRespository;
import com.augusto.employees.repository.EmployeesRepository;

@Service
public class ClockInService {

    @Autowired
    private ClockInRespository clockInRespository;
    @Autowired
    private EmployeesRepository employeesRepository;

    public ClockInDto registerEntry() {
        if(clockInRespository.findByName(EntryOrLeft.ENTRADA).isPresent()){
            throw new EmployeeException(HttpStatus.BAD_REQUEST, "Seu ultimo registro foi uma entrada");
        }
        var point = new ClockIn();
        point.setEmployees(getEmployee());
        point.setEntryTime(LocalDateTime.now());
        point.setName(EntryOrLeft.ENTRADA);
        point.setLeftTime(point.getEntryTime());
        point.setWorkedHours(0.0);
        clockInRespository.save(point);
        return new ClockInDto(point);
    }

    public ClockInDto registerLeft() {
        var point = clockInRespository.findByName(EntryOrLeft.ENTRADA)
                .orElseThrow(() -> new EmployeeException(HttpStatus.BAD_REQUEST, "Você não registrou nenhuma entrada"));
        point.setEmployees(getEmployee());
        point.setLeftTime(LocalDateTime.now());
        point.setName(EntryOrLeft.SAIDA);
        var secondsWorked = (double)ChronoUnit.SECONDS.between(point.getEntryTime(), point.getLeftTime());
        System.out.println(secondsWorked);
        point.setWorkedHours(convertTohours(secondsWorked));
        clockInRespository.save(point);
        return new ClockInDto(point);
    }

    private Double convertTohours(Double secondsWorked){
        var workedHours = secondsWorked / 3600;
        return workedHours;
    }

    public ClockInDto editEntryTime(Long id, LocalDateTime time) {
        var point = clockInRespository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Clock in", "id", id));
        point.setEntryTime(time);
        clockInRespository.save(point);
        return new ClockInDto(point);
    }

    public Set<ClockInDto> getRegistry(String uniqueCode) {
        var employee = employeeByUniqueCode(uniqueCode);
        var clockInDto = employee.getPoints().stream().map(x -> new ClockInDto(x)).collect(Collectors.toSet());
        return clockInDto;
    }

    private Employees employeeByUniqueCode(String uniqueCode) {
        var employee = employeesRepository.findByUniqueCode(uniqueCode)
                .orElseThrow(() -> new ResourceNotFoundException("Employee", "Unique Code", uniqueCode));
        return employee;
    }

    public Employees getEmployee() {
        var principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var name = ((UserDetails) principal).getUsername();
        var employee = employeesRepository.findByCpfOrEmail(name, name);
        return employee.get();
    }

    public Optional<Double> totalWorkedHours(String uniqueCode) {
        var employee = employeeByUniqueCode(uniqueCode);
        var clockIns = employee.getPoints();
        var totalHours = clockIns.stream().map(x-> x.getWorkedHours()).reduce(Double::sum);
        return totalHours;
    }
}
