package com.centralisation.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
public class MessageDTO {
    private String username;
    private LocalDateTime startDate = LocalDateTime.now();
    private String message;
}