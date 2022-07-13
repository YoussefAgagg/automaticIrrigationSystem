package com.example.automaticirrigationsystem.service;

import com.example.automaticirrigationsystem.aop.logging.Loggable;
import com.example.automaticirrigationsystem.domain.Plot;
import com.example.automaticirrigationsystem.domain.enumeration.Status;
import com.example.automaticirrigationsystem.repository.PlotRepository;
import java.time.LocalDateTime;
import javax.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class SensorCallingScheduler {

  private static final int timeSleep = 1000 * 60;
  private final PlotRepository plotRepository;
  @Value("${tries.count}")
  private int triesCount = 10;

  @Async
  @Loggable
  public void tryToConnectToSensor(Plot plot, EntityManager entityManager) {
    log.debug("try to re connect to sensor  '{}'", plot);

    while (triesCount > 0) {
      log.debug("try to connect sensor {}", triesCount);
      triesCount--;
      entityManager.clear();
      plot = getPlot(plot.getId());

      if (plot.getPlotSensor().getStatus() == Status.UP) {
        log.debug("sensor is up now after {} tries", triesCount);
        plot.setHasAlert(false);
        plot.setSensorCallCount(0);
        plot.setLastSensorCallTime("");
        plot.setLastIrrigationTime(LocalDateTime.now().toString());
        plot.setStartIrrigationTime(LocalDateTime.now().toString());
        plot.setIsIrrigated(true);
        break;
      }

      plot.setSensorCallCount(plot.getSensorCallCount() + 1);
      plot.setLastSensorCallTime(LocalDateTime.now().toString());

      try {
        Thread.sleep(timeSleep);
      } catch (InterruptedException e) {
        log.error("interrupted {}", e);
      }
      plotRepository.save(plot);

    }

    if (triesCount == 0) {
      plot.setHasAlert(true);
    }

    plotRepository.save(plot);

  }

  @Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW)
  Plot getPlot(Long id) {

    Plot plot = plotRepository.findById(id).get();
    plotRepository.refresh(plot);
    return plot;
  }


}
