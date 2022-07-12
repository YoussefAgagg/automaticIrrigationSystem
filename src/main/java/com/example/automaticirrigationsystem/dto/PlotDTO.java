package com.example.automaticirrigationsystem.dto;

import com.example.automaticirrigationsystem.domain.Sensor;
import com.example.automaticirrigationsystem.domain.Slot;
import com.example.automaticirrigationsystem.domain.enumeration.CropType;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 * A DTO for the {@link com.example.automaticirrigationsystem.domain.Plot} entity.
 */
@Data
public class PlotDTO implements Serializable {

  private Long Id;

  @NotNull
  private String plotCode;

  @NotNull
  private Double plotLength;

  @NotNull
  private Double plotWidth;

  private Boolean isIrrigated;

  private Integer startTriesCount;

  private Boolean hasAlert;

  private String startIrrigationTime;

  private String lastIrrigationTime;

  private int waterAmount;

  private CropType cropType;

  private Sensor plotSensor;

  private List<Slot> plotTimerSlots = new ArrayList<Slot>();


}
