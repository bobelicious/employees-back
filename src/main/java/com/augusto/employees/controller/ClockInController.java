package com.augusto.employees.controller;

import java.time.LocalDateTime;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.augusto.employees.payload.ClockInDto;
import com.augusto.employees.service.ClockInService;

@RestController
@RequestMapping ("api/v1/clock_in")
public class ClockInController {
    
    @Autowired
    private ClockInService clockInService;

    @PostMapping("/entry_register")
    public ResponseEntity<ClockInDto> registryEntry(){
        return new ResponseEntity<ClockInDto>(clockInService.registerEntry(), HttpStatus.CREATED);
    }

    @PostMapping("/left_register")
    public ResponseEntity<ClockInDto> registryLeft(){
        return new ResponseEntity<ClockInDto>(clockInService.registerLeft(), HttpStatus.CREATED);
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<ClockInDto> editRegistry(@RequestBody LocalDateTime time, @PathVariable Long id){
        return ResponseEntity.ok().body(clockInService.editEntryTime(id, time));
    }

    @GetMapping("/my_clock_in/{uniqueCode}")
    public ResponseEntity<Set<ClockInDto>> myClockIns(@PathVariable String uniqueCode){
        return ResponseEntity.ok().body(clockInService.getRegistry(uniqueCode));
    }
}
