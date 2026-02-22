package com.noom.interview.fullstack.sleep.controller.sleep;

import com.noom.interview.fullstack.sleep.dto.CreateSleepLogRequestDto;
import com.noom.interview.fullstack.sleep.dto.SleepLogResponseDto;
import com.noom.interview.fullstack.sleep.service.SleepLogService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;
import java.util.UUID;

@Validated
@RestController
@RequestMapping("/api/users/{userId}/sleep-logs")
public class SleepLogController {

    private final SleepLogService sleepLogService;

    public SleepLogController(SleepLogService sleepLogService) {
        this.sleepLogService = sleepLogService;
    }

    @PostMapping
    public ResponseEntity<SleepLogResponseDto> create(@PathVariable UUID userId,
                                                      @Valid @RequestBody CreateSleepLogRequestDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(sleepLogService.create(userId, dto));
    }

    @GetMapping
    public ResponseEntity<List<SleepLogResponseDto>> getLastNights(
            @PathVariable UUID userId,
            @RequestParam(defaultValue = "1") @Min(1) @Max(365) int nights) {
        return ResponseEntity.ok(sleepLogService.getLastNights(userId, nights));
    }
}
