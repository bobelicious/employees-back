package com.augusto.employees.controller;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.augusto.employees.model.ClockIn;
import com.augusto.employees.service.ClockInService;

@RestController
@RequestMapping ("api/v1/clock_in")
public class ClockInController {
    
    @Autowired
    private ClockInService clockInService;

    @PostMapping("/entry_register")
    public ResponseEntity<ClockIn> registryEntry(){
        return new ResponseEntity<ClockIn>(clockInService.registerEntry(), HttpStatus.CREATED);
    }

    @PostMapping("/left_register")
    public ResponseEntity<ClockIn> registryLeft(){
        return new ResponseEntity<ClockIn>(clockInService.registerLeft(), HttpStatus.CREATED);
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<ClockIn> editRegistry(@RequestBody LocalDateTime time, @PathVariable Long id){
        return ResponseEntity.ok().body(clockInService.editRegister(id, time));
    }
}
