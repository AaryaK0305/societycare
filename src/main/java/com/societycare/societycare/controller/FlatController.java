package com.societycare.societycare.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.societycare.societycare.entity.Flat;
import com.societycare.societycare.service.FlatService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/flats")
@RequiredArgsConstructor
public class FlatController {

    private final FlatService flatService;

    @PostMapping
    public ResponseEntity<Flat> createFlat(@Valid @RequestBody Flat flat) {
        Flat createdFlat = flatService.createFlat(flat);
        return new ResponseEntity<>(createdFlat, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Flat>> getAllFlats(
            @RequestParam(required = false) String building) {

        if (building != null && !building.isBlank()) {
            return ResponseEntity.ok(flatService.getFlatsByBuilding(building));
        }

        return ResponseEntity.ok(flatService.getAllFlats());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Flat> getFlatById(@PathVariable Long id) {
        return flatService.getFlatById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Flat> updateFlat(
            @PathVariable Long id,
            @RequestBody Flat flat) {

        Flat updatedFlat = flatService.updateFlat(id, flat);
        return ResponseEntity.ok(updatedFlat);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFlat(@PathVariable Long id) {
        flatService.deleteFlat(id);
        return ResponseEntity.noContent().build();
    }
}