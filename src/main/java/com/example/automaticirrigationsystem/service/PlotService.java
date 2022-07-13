package com.example.automaticirrigationsystem.service;

import com.example.automaticirrigationsystem.aop.logging.Loggable;
import com.example.automaticirrigationsystem.domain.Plot;
import com.example.automaticirrigationsystem.domain.Slot;
import com.example.automaticirrigationsystem.domain.enumeration.Status;
import com.example.automaticirrigationsystem.dto.PlotConfigDTO;
import com.example.automaticirrigationsystem.dto.PlotDTO;
import com.example.automaticirrigationsystem.repository.PlotRepository;
import com.example.automaticirrigationsystem.service.mapper.PlotMapper;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Plot}.
 */
@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class PlotService {

  private final PlotRepository plotRepository;

  private final PlotMapper plotMapper;


  /**
   * Save a plot.
   *
   * @param plotConfigDTO the plot to save.
   * @return the persisted plot.
   */
  @Loggable
  public Optional<PlotDTO> configurePlot(PlotConfigDTO plotConfigDTO, long id) {
    log.debug("Request to save Plot : {}", plotConfigDTO);
    Optional<Plot> existPlot = plotRepository.findById(id);

    return existPlot.map(existingPlot -> {
          existingPlot.setCropType(plotConfigDTO.getCropType());
          existingPlot.setWaterAmount(plotConfigDTO.getWaterAmount());
          List<Slot> slots = new ArrayList<>();
          for (int i = 0; i < plotConfigDTO.getSlotsCount(); i++) {
            Slot slot = new Slot();
            slot.setStatus(Status.UP);
            slots.add(slot);
          }
          existingPlot.setPlotTimerSlots(slots);
          return existingPlot;
        })
        .map(plotRepository::save)
        .map(plotMapper::toDto);

  }

  /**
   * Save a plot.
   *
   * @param plotDTO the plot to save.
   * @return the persisted plot.
   */
  @Loggable
  public PlotDTO save(PlotDTO plotDTO) {
    log.debug("Request to save Plot : {}", plotDTO);
    plotDTO.setId(null);
    Plot plot = plotMapper.toEntity(plotDTO);
    plot = plotRepository.save(plot);
    return plotMapper.toDto(plot);
  }

  @Loggable
  public PlotDTO update(PlotDTO plotDTO) {
    log.debug("Request to update Plot : {}", plotDTO);
    Plot plot = plotMapper.toEntity(plotDTO);
    plot = plotRepository.save(plot);
    return plotMapper.toDto(plot);
  }

  /**
   * Partially update a plot.
   *
   * @param plotDTO the plot to update partially.
   * @return the persisted plot.
   */
  @Loggable
  public Optional<PlotDTO> partialUpdate(PlotDTO plotDTO) {
    log.debug("Request to partially update Plot : {}", plotDTO);

    return plotRepository
        .findById(plotDTO.getId())
        .map(existingPlot -> {
          plotMapper.partialUpdate(existingPlot, plotDTO);

          return existingPlot;
        })
        .map(plotRepository::save)
        .map(plotMapper::toDto);
  }

  /**
   * Get all the plots.
   *
   * @param pageable the pagination information.
   * @return the list of plots.
   */
  @Loggable
  @Transactional(readOnly = true)
  public Page<PlotDTO> findAll(Pageable pageable) {
    log.debug("Request to get all Plots");
    return plotRepository.findAll(pageable).map(plotMapper::toDto);
  }

  /**
   * Get one plot by id.
   *
   * @param id the id of the plot.
   * @return the plot.
   */
  @Loggable
  @Transactional(readOnly = true)
  public Optional<PlotDTO> findOne(Long id) {
    log.debug("Request to get Plot : {}", id);
    return plotRepository.findById(id).map(plotMapper::toDto);
  }

  /**
   * Delete the plot by id.
   *
   * @param id the id of the plot.
   */
  @Loggable
  public void delete(Long id) {
    log.debug("Request to delete Plot : {}", id);
    plotRepository.deleteById(id);
  }


}
