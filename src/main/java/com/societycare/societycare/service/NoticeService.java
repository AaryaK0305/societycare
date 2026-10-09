package com.societycare.societycare.service;

import com.societycare.societycare.entity.Notice;
import com.societycare.societycare.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NoticeService {

    private final NoticeRepository noticeRepository;

    public Notice createNotice(Notice notice) {
        return noticeRepository.save(notice);
    }

    public List<Notice> getAllNotices() {
        return noticeRepository.findAll();
    }

    public Optional<Notice> getNoticeById(Long id) {
        return noticeRepository.findById(id);
    }

    public Notice updateNotice(Long id, Notice updatedNotice) {
        return noticeRepository.findById(id)
                .map(existingNotice -> {
                    if (updatedNotice.getTitle() != null) {
                        existingNotice.setTitle(updatedNotice.getTitle());
                    }
                    if (updatedNotice.getMessage() != null) {
                        existingNotice.setMessage(updatedNotice.getMessage());
                    }
                    return noticeRepository.save(existingNotice);
                })
                .orElseThrow(() -> new RuntimeException(
                        "Notice not found with id: " + id));
    }

    public void deleteNotice(Long id) {
        if (!noticeRepository.existsById(id)) {
            throw new RuntimeException(
                    "Notice not found with id: " + id);
        }
        noticeRepository.deleteById(id);
    }
}
