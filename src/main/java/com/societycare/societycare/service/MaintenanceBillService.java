package com.societycare.societycare.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.societycare.societycare.entity.MaintenanceBill;
import com.societycare.societycare.repository.MaintenanceBillRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MaintenanceBillService {

    private final MaintenanceBillRepository maintenanceBillRepository;

    public MaintenanceBill createBill(MaintenanceBill bill) {
        return maintenanceBillRepository.save(bill);
    }

    public List<MaintenanceBill> getAllBills() {
        return maintenanceBillRepository.findAll();
    }

    public Optional<MaintenanceBill> getBillById(Long id) {
        return maintenanceBillRepository.findById(id);
    }

    public List<MaintenanceBill> getBillsByMonth(String month) {
        return maintenanceBillRepository.findByMonth(month);
    }

    public List<MaintenanceBill> getBillsByStatus(String status) {
        return maintenanceBillRepository.findByStatus(status);
    }

    public MaintenanceBill updateBill(Long id, MaintenanceBill updatedBill) {
        return maintenanceBillRepository.findById(id)
                .map(existingBill -> {
                    if (updatedBill.getMonth() != null) {
                        existingBill.setMonth(updatedBill.getMonth());
                    }
                    if (updatedBill.getAmount() != null) {
                        existingBill.setAmount(updatedBill.getAmount());
                    }
                    if (updatedBill.getDueDate() != null) {
                        existingBill.setDueDate(updatedBill.getDueDate());
                    }
                    if (updatedBill.getStatus() != null) {
                        existingBill.setStatus(updatedBill.getStatus());
                    }
                    return maintenanceBillRepository.save(existingBill);
                })
                .orElseThrow(() -> new RuntimeException(
                        "Maintenance bill not found with id: " + id));
    }

    public void deleteBill(Long id) {
        if (!maintenanceBillRepository.existsById(id)) {
            throw new RuntimeException(
                    "Maintenance bill not found with id: " + id);
        }
        maintenanceBillRepository.deleteById(id);
    }
}