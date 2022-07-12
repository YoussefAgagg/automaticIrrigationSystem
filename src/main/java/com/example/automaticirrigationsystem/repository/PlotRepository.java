package com.example.automaticirrigationsystem.repository;


import com.example.automaticirrigationsystem.domain.Plot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Plot entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PlotRepository extends JpaRepository<Plot, Long> {

}
