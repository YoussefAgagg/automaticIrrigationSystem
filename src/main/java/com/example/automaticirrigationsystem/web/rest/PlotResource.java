package com.example.automaticirrigationsystem.web.rest;


import com.example.automaticirrigationsystem.aop.logging.Loggable;
import com.example.automaticirrigationsystem.exception.BadRequestException;
import com.example.automaticirrigationsystem.exception.ResourceDoesntExistException;
import com.example.automaticirrigationsystem.repository.PlotRepository;
import com.example.automaticirrigationsystem.service.PlotService;
import com.example.automaticirrigationsystem.service.dto.PlotDTO;
import com.example.automaticirrigationsystem.web.rest.util.PaginationUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * REST controller for managing {@link com.example.automaticirrigationsystem.domain.Plot}.
 */
@RestController
@RequestMapping("/api")
@Slf4j
@RequiredArgsConstructor
public class PlotResource {

    private final PlotService plotService;

    private final PlotRepository plotRepository;

    /**
     * {@code POST  /plots} : Create a new plot.
     *
     * @param plotDTO the plotDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new plotDTO, or with status {@code 400 (Bad Request)} if the plot has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/plots")
    @Loggable
    public ResponseEntity<PlotDTO> createPlot(@Valid @RequestBody PlotDTO plotDTO) throws URISyntaxException {
        log.debug("REST request to save Plot : {}", plotDTO);
        if (plotDTO.getId() != null) {
            throw new BadRequestException("A new plot cannot already have an ID");
        }
        PlotDTO result = plotService.save(plotDTO);
        return ResponseEntity
            .created(new URI("/api/plots/" + result.getId()))
            .body(result);
    }

    /**
     * {@code PUT  /plots/:id} : Updates an existing plot.
     *
     * @param id the id of the plotDTO to save.
     * @param plotDTO the plotDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated plotDTO,
     * or with status {@code 400 (Bad Request)} if the plotDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the plotDTO couldn't be updated.
     */
    @PutMapping("/plots/{id}")
    @Loggable
    public ResponseEntity<PlotDTO> updatePlot(
        @PathVariable final Long id,
        @Valid @RequestBody PlotDTO plotDTO)  {
        log.debug("REST request to update Plot : {}, {}", id, plotDTO);
        if (plotDTO.getId() == null) {
            throw new BadRequestException("Invalid id id null");
        }
        if (!Objects.equals(id, plotDTO.getId())) {
            throw new BadRequestException("Invalid ID id invalid");
        }

        if (!plotRepository.existsById(id)) {
            throw new BadRequestException("Plot not found id not found");
        }

        PlotDTO result = plotService.save(plotDTO);
        return ResponseEntity
            .ok().body(result);
    }

    /**
     * {@code GET  /plots} : get all the plots.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of plots in body.
     */
    @GetMapping("/plots")
    @Loggable
    public ResponseEntity<List<PlotDTO>> getAllPlots(@PageableDefault(sort = { "id" })Pageable pageable) {
        log.debug("REST request to get a page of Plots");
        Page<PlotDTO> page = plotService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /plots/:id} : get the "id" plot.
     *
     * @param id the id of the plotDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the plotDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/plots/{id}")
    @Loggable
    public ResponseEntity<PlotDTO> getPlot(@PathVariable Long id) {
        log.debug("REST request to get Plot : {}", id);
        Optional<PlotDTO> plotDTO = plotService.findOne(id);
        return plotDTO.map(plot->ResponseEntity.ok().body(plot))
                .orElseThrow(()->{throw new ResourceDoesntExistException("plot doesn't exist");});
    }

    /**
     * {@code DELETE  /plots/:id} : delete the "id" plot.
     *
     * @param id the id of the plotDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/plots/{id}")
    @Loggable
    public ResponseEntity<Void> deletePlot(@PathVariable Long id) {
        log.debug("REST request to delete Plot : {}", id);
        plotService.delete(id);
        return ResponseEntity
            .noContent().build();
    }
}