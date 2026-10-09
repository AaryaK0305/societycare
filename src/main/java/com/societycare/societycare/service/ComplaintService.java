package com.societycare.societycare.service;

import com.societycare.societycare.entity.Complaint;
import com.societycare.societycare.repository.ComplaintRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ComplaintService {

    private final ComplaintRepository complaintRepository;

    public Complaint createComplaint(Complaint complaint) {
        return complaintRepository.save(complaint);
    }

    public List<Complaint> getAllComplaints() {
        return complaintRepository.findAll();
    }

    public Optional<Complaint> getComplaintById(Long id) {
        return complaintRepository.findById(id);
    }

    public Complaint updateComplaint(Long id, Complaint updatedComplaint) {
        return complaintRepository.findById(id)
                .map(existingComplaint -> {
                    if (updatedComplaint.getTitle() != null) {
                        existingComplaint.setTitle(updatedComplaint.getTitle());
                    }
                    if (updatedComplaint.getDescription() != null) {
                        existingComplaint.setDescription(updatedComplaint.getDescription());
                    }
                    if (updatedComplaint.getStatus() != null) {
                        existingComplaint.setStatus(updatedComplaint.getStatus());
                    }
                    return complaintRepository.save(existingComplaint);
                })
                .orElseThrow(() -> new RuntimeException(
                        "Complaint not found with id: " + id));
    }

    public void deleteComplaint(Long id) {
        if (!complaintRepository.existsById(id)) {
            throw new RuntimeException(
                    "Complaint not found with id: " + id);
        }
        complaintRepository.deleteById(id);
    }
}
