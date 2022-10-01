package com.augusto.employees.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.augusto.employees.payload.EmployeeDto;
import com.augusto.employees.service.EmployeeService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("api/v1/employees")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    @PostMapping(value="/create")
    public ResponseEntity<?> postEmployees(@RequestPart EmployeeDto employeeDto, @RequestParam(required = false) MultipartFile file) {
        try {
            return new ResponseEntity<>(employeeService.createEmployee(employeeDto, file), HttpStatus.CREATED);
        } catch (IOException e) {
           return ResponseEntity.unprocessableEntity().body(e.getMessage());
        }
    }

    @GetMapping
    public Page<EmployeeDto> getAllEmployees(@PageableDefault(sort= {"name", "cpf"}, size = 15, page = 0) Pageable pageable){
        return employeeService.listAll(pageable);
    }

    @GetMapping(value = "/my_page")
    public EmployeeDto getById(){
        return employeeService.my_page();
    }

    @GetMapping(value="/cpf/{cpf}")
    public ResponseEntity<EmployeeDto> getByCpf(@PathVariable String cpf){
        return ResponseEntity.ok().body(employeeService.findByCpf(cpf));
    }

    @GetMapping(value="/email/{email}")
    public ResponseEntity<EmployeeDto> getByEmail(@PathVariable String email){
        return ResponseEntity.ok().body(employeeService.findByEmail(email));
    }

    @PutMapping(value="/update/{id}")
    public ResponseEntity<EmployeeDto> putEmployee(@RequestBody EmployeeDto employeeDto, @PathVariable Long id){
        return new ResponseEntity<EmployeeDto>(employeeService.putEmployee(id, employeeDto), HttpStatus.OK);
    }

    @DeleteMapping(value="/delete/{id}")
    public ResponseEntity<?> deleteById(@PathVariable Long id){
        employeeService.removeEmployee(id);
        return ResponseEntity.noContent().build();
    }

    
}
