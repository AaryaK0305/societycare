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

import com.societycare.societycare.entity.MaintenanceBill;
import com.societycare.societycare.service.MaintenanceBillService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/maintenance-bills")
@RequiredArgsConstructor
public class MaintenanceBillController {

    private final MaintenanceBillService maintenanceBillService;

    @PostMapping
    public ResponseEntity<MaintenanceBill> createBill(@Valid @RequestBody MaintenanceBill bill) {
        MaintenanceBill createdBill = maintenanceBillService.createBill(bill);
        return new ResponseEntity<>(createdBill, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<MaintenanceBill>> getAllBills(
            @RequestParam(required = false) String month,
            @RequestParam(required = false) String status) {

        if (month != null && !month.isBlank()) {
            return ResponseEntity.ok(maintenanceBillService.getBillsByMonth(month));
        }

        if (status != null && !status.isBlank()) {
            return ResponseEntity.ok(maintenanceBillService.getBillsByStatus(status));
        }

        return ResponseEntity.ok(maintenanceBillService.getAllBills());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MaintenanceBill> getBillById(@PathVariable Long id) {
        return maintenanceBillService.getBillById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<MaintenanceBill> updateBill(
            @PathVariable Long id,
            @RequestBody MaintenanceBill bill) {

        MaintenanceBill updatedBill = maintenanceBillService.updateBill(id, bill);
        return ResponseEntity.ok(updatedBill);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBill(@PathVariable Long id) {
        maintenanceBillService.deleteBill(id);
        return ResponseEntity.noContent().build();
    }
}