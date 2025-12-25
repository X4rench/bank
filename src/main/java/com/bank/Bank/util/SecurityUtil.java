package com.bank.Bank.util;

import com.bank.Bank.model.User;
import com.bank.Bank.model.UserPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Утилитный класс для работы с Spring Security
 */
public final class SecurityUtil {

    private SecurityUtil() {
        // Утилитный класс - запрет создания экземпляров
    }

    /**
     * Получить текущего аутентифицированного пользователя
     * @return User или null, если пользователь не аутентифицирован
     */
    public static User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }

        Object principal = authentication.getPrincipal();
        
        if (principal instanceof UserPrincipal) {
            return ((UserPrincipal) principal).getUser();
        }
        
        return null;
    }

    /**
     * Получить ID текущего аутентифицированного пользователя
     * @return ID пользователя или null, если пользователь не аутентифицирован
     */
    public static Long getCurrentUserId() {
        User user = getCurrentUser();
        return user != null ? user.getId() : null;
    }

    /**
     * Проверить, аутентифицирован ли пользователь
     * @return true, если пользователь аутентифицирован
     */
    public static boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && authentication.isAuthenticated();
    }
}

