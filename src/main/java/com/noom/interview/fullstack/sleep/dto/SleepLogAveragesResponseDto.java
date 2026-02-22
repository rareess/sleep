package com.noom.interview.fullstack.sleep.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SleepLogAveragesResponseDto {

    private LocalDate startDate;
    private LocalDate endDate;
    private int averageMinutesInBed;
    private LocalTime averageBedTime;
    private LocalTime averageWakeTime;
    private int badCount;
    private int okCount;
    private int goodCount;
}
