package com.example.vinicius.controller;


import com.example.vinicius.dto.NotificationResponseDTO;
import com.example.vinicius.dto.UserSummaryDTO;
import com.example.vinicius.entity.User;
import com.example.vinicius.service.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/notifications")
public class NotificationController {
    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService){
        this.notificationService = notificationService;
    }

    @GetMapping
    public ResponseEntity<List<NotificationResponseDTO>> getMyNotifications(@AuthenticationPrincipal User loggedUser){
        List<NotificationResponseDTO> dtos = notificationService.getUserNotifications(loggedUser).stream()
                .map(notification -> {
                    User s = notification.getSender();
                    String nickname = s.getProfile() != null ? s.getProfile().getNickname(): s.getName();
                    String photo =  s.getProfile() != null ? s.getProfile().getPhotoUrl(): null;
                    UserSummaryDTO senderDTO = new UserSummaryDTO(s.getId(), nickname, photo);

                    return new NotificationResponseDTO(
                            notification.getId(),
                            senderDTO,
                            notification.getType(),
                            notification.isRead(),
                            notification.getCreatedAt()
                    );
                })
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @PutMapping("/{id}/read")
    public ResponseEntity<Void> markAsRead(@PathVariable Long id, @AuthenticationPrincipal User loggedUser){
        notificationService.markAsRead(id, loggedUser);
        return ResponseEntity.ok().build();
    }
}
