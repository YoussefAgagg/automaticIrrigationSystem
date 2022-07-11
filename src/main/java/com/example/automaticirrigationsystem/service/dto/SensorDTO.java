package com.example.automaticirrigationsystem.service.dto;

import com.example.automaticirrigationsystem.domain.enumeration.Status;
import lombok.Data;

import java.io.Serializable;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.example.automaticirrigationsystem.domain.Sensor} entity.
 */
@Data
public class SensorDTO implements Serializable {

    private Long id;

    @NotNull
    private String code;

    private Status status;

}
