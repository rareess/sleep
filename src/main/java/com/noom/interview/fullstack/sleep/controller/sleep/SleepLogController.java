package com.noom.interview.fullstack.sleep.controller.sleep;

import com.noom.interview.fullstack.sleep.dto.CreateSleepLogRequestDto;
import com.noom.interview.fullstack.sleep.dto.SleepLogAveragesResponseDto;
import com.noom.interview.fullstack.sleep.dto.SleepLogResponseDto;
import com.noom.interview.fullstack.sleep.service.SleepLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.time.Clock;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Tag(name = "Sleep Logs")
@Validated
@RestController
@RequestMapping("/api/users/{userId}/sleep-logs")
public class SleepLogController {

    private final SleepLogService sleepLogService;
    private final Clock clock;

    public SleepLogController(SleepLogService sleepLogService, Clock clock) {
        this.sleepLogService = sleepLogService;
        this.clock = clock;
    }

    @Operation(summary = "Create a sleep log for a user")
    @PostMapping
    public ResponseEntity<SleepLogResponseDto> create(@PathVariable UUID userId,
                                                      @Valid @RequestBody CreateSleepLogRequestDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(sleepLogService.create(userId, dto));
    }

    @Operation(summary = "Get last N nights of sleep logs for a user")
    @GetMapping
    public ResponseEntity<List<SleepLogResponseDto>> getLastNights(
            @PathVariable UUID userId,
            @RequestParam(defaultValue = "1") @Min(1) @Max(365) int nights) {
        LocalDate endDate = LocalDate.now(clock).minusDays(1);
        LocalDate startDate = LocalDate.now(clock).minusDays(nights);
        return ResponseEntity.ok(sleepLogService.getLastNights(userId, startDate, endDate));
    }

    @Operation(summary = "Get sleep averages for a user over the last N days")
    @GetMapping("/averages")
    public ResponseEntity<SleepLogAveragesResponseDto> getAverages(
            @PathVariable UUID userId,
            @RequestParam(defaultValue = "30") @Min(1) @Max(365) int days) {
        LocalDate endDate = LocalDate.now(clock).minusDays(1);
        LocalDate startDate = LocalDate.now(clock).minusDays(days);
        return ResponseEntity.ok(sleepLogService.getAverages(userId, startDate, endDate));
    }
}
