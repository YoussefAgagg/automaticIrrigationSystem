package com.example.automaticirrigationsystem.repository;

import com.example.automaticirrigationsystem.domain.Plot;
import java.util.List;

public interface PlotRepository extends CustomRepository<Plot, Long> {

  List<Plot> findAllByHasAlertIsTrue();
}
