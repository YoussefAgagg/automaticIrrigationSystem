package com.example.automaticirrigationsystem.service.dto;

import com.example.automaticirrigationsystem.domain.enumeration.CropType;
import lombok.Data;

import java.io.Serializable;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.example.automaticirrigationsystem.domain.Plot} entity.
 */
@Data
public class PlotDTO implements Serializable {

    private Long id;

    @NotNull
    private String code;

    private Double length;

    private Double width;

    private Boolean isIrrigated;

    private Integer triesCount;

    private Boolean hasAlert;

    private CropType cropType;

    private SensorDTO sensor;

}
