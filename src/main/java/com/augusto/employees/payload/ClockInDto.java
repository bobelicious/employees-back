package com.augusto.employees.payload;

import java.time.LocalDateTime;

import com.augusto.employees.model.ClockIn;
import com.augusto.employees.model.EntryOrLeft;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClockInDto {
    private Long id;
    private EntryOrLeft name;
    private String employees;
    private LocalDateTime entryTime;
    private LocalDateTime leftTime;
    private int workedHours;

    public ClockInDto(ClockIn clockIn) {
        this.id = clockIn.getId();
        this.name = clockIn.getName();
        this.employees = clockIn.getEmployees().getName();
        this.entryTime = clockIn.getEntryTime();
        this.leftTime  = clockIn.getLeftTime();
        this.workedHours = clockIn.getWorkedHours();
    }
}
