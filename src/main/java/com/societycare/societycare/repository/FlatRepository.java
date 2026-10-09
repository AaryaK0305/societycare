package com.societycare.societycare.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.societycare.societycare.entity.Flat;

@Repository
public interface FlatRepository extends JpaRepository<Flat, Long> {

    List<Flat> findByBuilding(String building);

    Optional<Flat> findByBuildingAndFlatNumber(String building, String flatNumber);

    boolean existsByBuildingAndFlatNumber(String building, String flatNumber);
}