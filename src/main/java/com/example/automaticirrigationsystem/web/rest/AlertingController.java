package com.example.automaticirrigationsystem.web.rest;


import com.example.automaticirrigationsystem.aop.logging.Loggable;
import com.example.automaticirrigationsystem.dto.PlotDTO;
import com.example.automaticirrigationsystem.service.PlotService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for manipulating irrigation business.
 */
@RestController
@RequestMapping("/api")
@Slf4j
@RequiredArgsConstructor
public class AlertingController {

  private final PlotService plotService;

  /**
   * {@code GET  /alert} : show up all plots with alarm.
   *
   * @return the {@link ResponseEntity} with status {@code 200 (Ok)} and with body the new
   * sensorDTO, or with status {@code 400 (Not Found)} if the plot does not exist.
   */
  @GetMapping("/alert")
  @Loggable
  public ResponseEntity<List<PlotDTO>> startPlotIrrigation(
      @PathVariable(value = "id") final Long plotId) {
    log.debug("REST request to start irrigation : {}", plotId);
    var result = plotService.getAllPlotsHasAlarm();
    return ResponseEntity.ok().body(result);

  }


}
