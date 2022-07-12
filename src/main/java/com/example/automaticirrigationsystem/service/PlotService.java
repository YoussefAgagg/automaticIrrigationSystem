package com.example.automaticirrigationsystem.service;

import java.util.Optional;

import com.example.automaticirrigationsystem.aop.logging.Loggable;
import com.example.automaticirrigationsystem.domain.Plot;
import com.example.automaticirrigationsystem.repository.PlotRepository;
import com.example.automaticirrigationsystem.service.dto.PlotDTO;
import com.example.automaticirrigationsystem.service.mapper.PlotMapper;
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
     * @param plotDTO the plot to save.
     * @return the persisted plot.
     */
    @Loggable
    public PlotDTO save(PlotDTO plotDTO) {
        log.debug("Request to save Plot : {}", plotDTO);
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