package com.example.automaticirrigationsystem.service.dto;

import com.example.automaticirrigationsystem.domain.enumeration.Status;
import lombok.Data;

import java.io.Serializable;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.example.automaticirrigationsystem.domain.Slot} entity.
 */
@Data
public class SlotDTO implements Serializable {

    private Long id;

    @NotNull
    private String code;

    private Status status;

    private PlotDTO plot;

}
