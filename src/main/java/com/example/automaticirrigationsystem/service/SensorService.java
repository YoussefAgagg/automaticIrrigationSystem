package com.example.automaticirrigationsystem.service;

import com.example.automaticirrigationsystem.aop.logging.Loggable;
import com.example.automaticirrigationsystem.domain.Sensor;
import com.example.automaticirrigationsystem.dto.SensorDTO;
import com.example.automaticirrigationsystem.repository.SensorRepository;
import com.example.automaticirrigationsystem.service.mapper.SensorMapper;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Sensor}.
 */
@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class SensorService {

  private final SensorRepository sensorRepository;

  private final SensorMapper sensorMapper;

  /**
   * Save a sensor.
   *
   * @param sensorDTO the sensor to save.
   * @return the persisted sensor.
   */
  @Loggable
  public SensorDTO save(SensorDTO sensorDTO) {
    log.debug("Request to save Sensor : {}", sensorDTO);
    Sensor sensor = sensorMapper.toEntity(sensorDTO);
    sensor = sensorRepository.save(sensor);
    return sensorMapper.toDto(sensor);
  }

  /**
   * Partially update a sensor.
   *
   * @param sensorDTO the sensor to update partially.
   * @return the persisted sensor.
   */
  @Loggable
  public Optional<SensorDTO> partialUpdate(SensorDTO sensorDTO) {
    log.debug("Request to partially update Sensor : {}", sensorDTO);

    return sensorRepository
        .findById(sensorDTO.getId())
        .map(existingSensor -> {
          sensorMapper.partialUpdate(existingSensor, sensorDTO);

          return existingSensor;
        })
        .map(sensorRepository::save)
        .map(sensorMapper::toDto);
  }

  /**
   * Get all the sensors.
   *
   * @param pageable the pagination information.
   * @return the list of sensors.
   */
  @Loggable
  @Transactional(readOnly = true)
  public Page<SensorDTO> findAll(Pageable pageable) {
    log.debug("Request to get all Sensors");
    return sensorRepository.findAll(pageable).map(sensorMapper::toDto);
  }

  /**
   * Get one sensor by id.
   *
   * @param id the id of the sensor.
   * @return the sensor.
   */
  @Loggable
  @Transactional(readOnly = true)
  public Optional<SensorDTO> findOne(Long id) {
    log.debug("Request to get Sensor : {}", id);
    return sensorRepository.findById(id).map(sensorMapper::toDto);
  }

  /**
   * Delete the sensor by id.
   *
   * @param id the id of the sensor.
   */
  @Loggable
  public void delete(Long id) {
    log.debug("Request to delete Sensor : {}", id);
    sensorRepository.deleteById(id);
  }
}
