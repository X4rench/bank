package com.bank.Bank.controller;

import com.bank.Bank.model.Notification;
import com.bank.Bank.model.enums.NotificationType;
import com.bank.Bank.service.NotificationService;
import com.bank.Bank.util.EnumLocalizationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/api/notifications")
public class NotificationController extends BaseController {

    @Autowired
    private NotificationService notificationService;

    @GetMapping("/user/{userId}")
    @ResponseBody
    public ResponseEntity<List<Notification>> getNotificationsByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(notificationService.getNotificationsByUserId(userId));
    }

    @GetMapping("/user/{userId}/unread")
    @ResponseBody
    public ResponseEntity<List<Notification>> getUnreadNotificationsByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(notificationService.getUnreadNotificationsByUserId(userId));
    }

    @GetMapping("/user/{userId}/unread-count")
    @ResponseBody
    public ResponseEntity<Long> getUnreadCount(@PathVariable Long userId) {
        return ResponseEntity.ok(notificationService.getUnreadCount(userId));
    }

    @GetMapping("/page")
    public String notificationsPage(Model model) {
        Long userId = getCurrentUserId();
        List<Notification> notifications = notificationService.getNotificationsByUserId(userId)
            .stream()
            .sorted(Comparator.comparing(Notification::getCreatedAt).reversed())
            .collect(Collectors.toList());
        
        model.addAttribute("notifications", notifications);
        model.addAttribute("unreadCount", notificationService.getUnreadCount(userId));
        model.addAttribute("notificationTypeNames", EnumLocalizationUtil.getNotificationTypeNames());
        
        return "notifications";
    }

    @PostMapping
    @ResponseBody
    public ResponseEntity<Notification> createNotification(@RequestParam Long userId,
                                                         @RequestParam NotificationType type,
                                                         @RequestParam String title,
                                                         @RequestParam String message) {
        Notification notification = notificationService.createNotification(userId, type, title, message);
        return ResponseEntity.ok(notification);
    }

    @PostMapping("/{id}/read")
    public String markAsRead(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            notificationService.markAsRead(id);
            addSuccessMessage(redirectAttributes, "Уведомление отмечено как прочитанное");
        } catch (Exception e) {
            handleException(redirectAttributes, e);
        }
        return "redirect:/api/notifications/page";
    }

    @PostMapping("/user/{userId}/read-all")
    public String markAllAsRead(@PathVariable Long userId, RedirectAttributes redirectAttributes) {
        try {
            notificationService.markAllAsRead(userId);
            addSuccessMessage(redirectAttributes, "Все уведомления отмечены как прочитанные");
        } catch (Exception e) {
            handleException(redirectAttributes, e);
        }
        return "redirect:/api/notifications/page";
    }
}
