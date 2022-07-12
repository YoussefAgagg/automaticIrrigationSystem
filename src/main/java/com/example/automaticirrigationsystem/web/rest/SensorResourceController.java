package com.example.automaticirrigationsystem.web.rest;


import com.example.automaticirrigationsystem.aop.logging.Loggable;
import com.example.automaticirrigationsystem.dto.SensorDTO;
import com.example.automaticirrigationsystem.exception.BadRequestException;
import com.example.automaticirrigationsystem.exception.ResourceNotFoundException;
import com.example.automaticirrigationsystem.repository.SensorRepository;
import com.example.automaticirrigationsystem.service.SensorService;
import com.example.automaticirrigationsystem.util.PaginationUtil;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

/**
 * REST controller for managing {@link com.example.automaticirrigationsystem.domain.Sensor}.
 */
@RestController
@RequestMapping("/api")
@Slf4j
@RequiredArgsConstructor
public class SensorResourceController {

    private final SensorService sensorService;

    private final SensorRepository sensorRepository;

    /**
     * {@code POST  /sensors} : Create a new sensor.
     *
     * @param sensorDTO the sensorDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new sensorDTO, or with status {@code 400 (Bad Request)} if the sensor has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/sensors")
    @Loggable
    public ResponseEntity<SensorDTO> createSensor(@Valid @RequestBody SensorDTO sensorDTO) throws URISyntaxException {
        log.debug("REST request to save Sensor : {}", sensorDTO);
        if (sensorDTO.getId() != null) {
            throw new BadRequestException("A new sensor cannot already have an ID");
        }
        SensorDTO result = sensorService.save(sensorDTO);
        return ResponseEntity
            .created(new URI("/api/sensors/" + result.getId()))
            .body(result);
    }

    /**
     * {@code PUT  /sensors/:id} : Updates an existing sensor.
     *
     * @param id the id of the sensorDTO to save.
     * @param sensorDTO the sensorDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sensorDTO,
     * or with status {@code 400 (Bad Request)} if the sensorDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the sensorDTO couldn't be updated.
     */
    @PutMapping("/sensors/{id}")
    @Loggable
    public ResponseEntity<SensorDTO> updateSensor(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SensorDTO sensorDTO ){
        log.debug("REST request to update Sensor : {}, {}", id, sensorDTO);
        if (sensorDTO.getId() == null) {
            throw new BadRequestException("Invalid id idnull");
        }
        if (!Objects.equals(id, sensorDTO.getId())) {
            throw new BadRequestException("Invalid ID idinvalid");
        }

        if (!sensorRepository.existsById(id)) {
            throw new BadRequestException("Sensor not found idnotfound");
        }

        SensorDTO result = sensorService.save(sensorDTO);
        return ResponseEntity
            .ok().body(result);
    }

    /**
     * {@code GET  /sensors} : get all the sensors.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of sensors in body.
     */
    @GetMapping("/sensors")
    @Loggable
    public ResponseEntity<List<SensorDTO>> getAllSensors( @PageableDefault(sort = { "id" })Pageable pageable) {

        log.debug("REST request to get a page of Sensors");
        Page<SensorDTO> page = sensorService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /sensors/:id} : get the "id" sensor.
     *
     * @param id the id of the sensorDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the sensorDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/sensors/{id}")
    @Loggable
    public ResponseEntity<SensorDTO> getSensor(@PathVariable Long id) {
        log.debug("REST request to get Sensor : {}", id);
        Optional<SensorDTO> sensorDTO = sensorService.findOne(id);
        return sensorDTO.map(sensor -> ResponseEntity.ok().body(sensor))
            .orElseThrow(() -> {
                throw new ResourceNotFoundException("sensor doesn't exist");
            });
    }

    /**
     * {@code DELETE  /sensors/:id} : delete the "id" sensor.
     *
     * @param id the id of the sensorDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/sensors/{id}")
    @Loggable
    public ResponseEntity<Void> deleteSensor(@PathVariable Long id) {
        log.debug("REST request to delete Sensor : {}", id);
        sensorService.delete(id);
        return ResponseEntity
            .noContent()
                .build();
    }
}
