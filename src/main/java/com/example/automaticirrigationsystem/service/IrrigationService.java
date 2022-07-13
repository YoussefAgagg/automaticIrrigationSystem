package com.example.automaticirrigationsystem.service;

import com.example.automaticirrigationsystem.aop.logging.Loggable;
import com.example.automaticirrigationsystem.domain.Plot;
import com.example.automaticirrigationsystem.domain.enumeration.Status;
import com.example.automaticirrigationsystem.dto.PlotDTO;
import com.example.automaticirrigationsystem.exception.BadRequestException;
import com.example.automaticirrigationsystem.exception.ResourceNotFoundException;
import com.example.automaticirrigationsystem.exception.SensorCantBeReachedException;
import com.example.automaticirrigationsystem.repository.PlotRepository;
import com.example.automaticirrigationsystem.service.mapper.PlotMapper;
import java.time.LocalDateTime;
import java.util.Optional;
import javax.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service implementation for manipulating plot irrigation {@link Plot}.
 */
@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class IrrigationService {

  private final PlotRepository plotRepository;
  private final PlotMapper plotMapper;
  private final SensorCallingScheduler sensorCallingScheduler;
  private final EntityManager entityManager;

  @Value("${tries.count}")
  private int triesCount = 10;

  /**
   * start irrigate a plot.
   *
   * @param plotId the plot id to start irrigate.
   * @return the persisted plot.
   */
  @Loggable
  public Optional<PlotDTO> startIrrigate(Long plotId) {
    log.debug("Request to start irrigate a Plot : {}", plotId);
    Optional<Plot> existPlot = plotRepository.findById(plotId);

    existPlot.ifPresentOrElse(plot -> {
      if (plot.getSensorCallCount() > 0) {
        throw new SensorCantBeReachedException(
            "Be patient!"
                + " sensor is scheduled to be called " + plot.getSensorCallCount() + "/"
                + triesCount + " " + plot.getLastSensorCallTime());
      } else {
        sensorCallingScheduler.tryToConnectToSensor(plot, entityManager);
      }

    }, () -> {
      throw new ResourceNotFoundException("plot doesn't exist!");
    });

    return plotRepository.findById(plotId).map(plotMapper::toDto);
  }

  @Loggable
  public Optional<PlotDTO> endIrrigate(Long plotId) {
    log.debug("Request to end irrigate a Plot : {}", plotId);
    Optional<Plot> existPlot = plotRepository.findById(plotId);

    return existPlot.map(plot -> {
      if (plot.getPlotSensor().getStatus() == Status.DOWN) {
        throw new BadRequestException("the plot sensor is down");
      }
      plot.setIsIrrigated(false);
      plot.setLastIrrigationTime(LocalDateTime.now().toString());

      return plot;
    }).map(plotMapper::toDto);

  }
}
