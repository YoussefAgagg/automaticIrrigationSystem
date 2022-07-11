package com.example.automaticirrigationsystem.service.mapper;

import com.example.automaticirrigationsystem.domain.Sensor;
import com.example.automaticirrigationsystem.service.dto.SensorDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Sensor} and its DTO {@link SensorDTO}.
 */
@Mapper(componentModel = "spring")
public interface SensorMapper extends EntityMapper<SensorDTO, Sensor> {
}
