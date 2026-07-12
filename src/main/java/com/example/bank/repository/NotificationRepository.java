package com.example.bank.repository;

import com.example.bank.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByMemberId(Long memberId);

    Optional<Notification> findByIdAndMemberId(Long id, Long memberId);

}
