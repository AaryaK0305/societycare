package com.societycare.societycare.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.societycare.societycare.entity.Flat;
import com.societycare.societycare.repository.FlatRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FlatService {

    private final FlatRepository flatRepository;

    public Flat createFlat(Flat flat) {
        if (flatRepository.existsByBuildingAndFlatNumber(
                flat.getBuilding(), flat.getFlatNumber())) {
            throw new IllegalArgumentException(
                    "Flat " + flat.getFlatNumber() +
                    " already exists in building " + flat.getBuilding());
        }

        return flatRepository.save(flat);
    }

    public List<Flat> getAllFlats() {
        return flatRepository.findAll();
    }

    public Optional<Flat> getFlatById(Long id) {
        return flatRepository.findById(id);
    }

    public List<Flat> getFlatsByBuilding(String building) {
        return flatRepository.findByBuilding(building);
    }

    public Flat updateFlat(Long id, Flat updatedFlat) {
        return flatRepository.findById(id)
                .map(existingFlat -> {
                    existingFlat.setFlatNumber(updatedFlat.getFlatNumber());
                    existingFlat.setBuilding(updatedFlat.getBuilding());
                    existingFlat.setFloor(updatedFlat.getFloor());

                    return flatRepository.save(existingFlat);
                })
                .orElseThrow(() ->
                        new RuntimeException("Flat not found with id: " + id));
    }

    public void deleteFlat(Long id) {
        if (!flatRepository.existsById(id)) {
            throw new RuntimeException("Flat not found with id: " + id);
        }

        flatRepository.deleteById(id);
    }
}