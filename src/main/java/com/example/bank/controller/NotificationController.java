package com.example.bank.controller;

import com.example.bank.dto.notification.NotificationReadResponse;
import com.example.bank.dto.notification.NotificationResponse;
import com.example.bank.security.CustomUserDetails;
import com.example.bank.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/notifications")
public class NotificationController {
    private final NotificationService notificationService;

    @GetMapping
    public ResponseEntity<List<NotificationResponse>> getNotifications(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ){
        List<NotificationResponse> notifications =
                notificationService.getNotifications(userDetails.getMemberId());

        return ResponseEntity.ok(notifications);
    }

    @PatchMapping("/{notificationId}")
    public ResponseEntity<NotificationReadResponse> setRead(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long notificationId
    ){
        NotificationReadResponse response =
                notificationService.setRead(notificationId, userDetails.getMemberId());

        return ResponseEntity.ok(response);
    }


}
