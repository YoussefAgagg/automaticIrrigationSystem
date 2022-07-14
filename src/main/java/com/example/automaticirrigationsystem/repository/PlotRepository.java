package com.example.automaticirrigationsystem.repository;

import com.example.automaticirrigationsystem.domain.Plot;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlotRepository extends JpaRepository<Plot, Long> {

  List<Plot> findAllByHasAlertIsTrue();
}
