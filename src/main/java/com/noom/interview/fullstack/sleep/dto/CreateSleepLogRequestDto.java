package com.noom.interview.fullstack.sleep.dto;

import com.noom.interview.fullstack.sleep.model.SleepStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
public class CreateSleepLogRequestDto {

    @NotNull(message = "Sleep date is required")
    @PastOrPresent(message = "Sleep date cannot be in the future")
    private LocalDate sleepDate;

    @NotNull(message = "Bed time is required")
    private LocalTime bedTime;

    @NotNull(message = "Wake time is required")
    private LocalTime wakeTime;

    @NotNull(message = "Sleep status is required")
    private SleepStatus sleepStatus;
}
