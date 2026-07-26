package com.example.bank.controller;

import com.example.bank.dto.notification.NotificationDetailResponse;
import com.example.bank.dto.notification.NotificationReadResponse;
import com.example.bank.dto.notification.NotificationResponse;
import com.example.bank.security.CustomUserDetails;
import com.example.bank.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
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
    @Operation(
            summary = "알림 조회",
            description = "로그인한 사용자의 알림 내역을 조회합니다.(타이틀만)"
    )
    public ResponseEntity<List<NotificationResponse>> getNotifications(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ){
        List<NotificationResponse> notifications =
                notificationService.getNotifications(userDetails.getMemberId());

        return ResponseEntity.ok(notifications);
    }

    @GetMapping("/{notificationId}")
    @Operation(
            summary = "알림 상세 조회",
            description = "선택한 알림의 메세지까지 포함하여 조회합니다."
    )
    public ResponseEntity<NotificationDetailResponse> getNotificationDetails(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long notificationId
    ){
        NotificationDetailResponse notificationDetails =
                notificationService.getNotificationDetails(notificationId, userDetails.getMemberId());

        return ResponseEntity.ok(notificationDetails);
    }

    @PatchMapping("/{notificationId}")
    @Operation(
            summary = "알림 읽음 처리",
            description = "로그인한 사용자가 알림 ID로 해당 알림을 읽음 처리 합니다."
    )
    public ResponseEntity<Void> setRead(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long notificationId
    ){
        notificationService.setRead(notificationId, userDetails.getMemberId());

        return ResponseEntity.noContent().build();
    }


}
