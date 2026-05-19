package cl.duoc.schedule.dto;

import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonFormat;

public record DoctorScheduleResponse(
    Long doctorScheduleId,
    Long doctorId,
    String dayOfWeek,
    
    @JsonFormat(pattern = "HH:mm:ss")
    LocalTime startTime,
    
    @JsonFormat(pattern = "HH:mm:ss")
    LocalTime endTime,
    
    Boolean active
) {}