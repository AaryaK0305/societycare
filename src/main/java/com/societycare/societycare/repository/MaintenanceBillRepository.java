package com.societycare.societycare.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.societycare.societycare.entity.MaintenanceBill;

@Repository
public interface MaintenanceBillRepository extends JpaRepository<MaintenanceBill, Long> {

    List<MaintenanceBill> findByMonth(String month);

    List<MaintenanceBill> findByStatus(String status);
}