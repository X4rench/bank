package com.bank.Bank.controller;

import com.bank.Bank.util.SecurityUtil;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Базовый контроллер с общими константами и методами
 */
public abstract class BaseController {

    /**
     * ID пользователя по умолчанию для демонстрации (fallback)
     * Используется только если пользователь не аутентифицирован
     */
    protected static final Long DEFAULT_USER_ID = 2L;

    /**
     * Добавить сообщение об успехе
     */
    protected void addSuccessMessage(RedirectAttributes redirectAttributes, String message) {
        redirectAttributes.addFlashAttribute("success", message);
    }

    /**
     * Добавить сообщение об ошибке
     */
    protected void addErrorMessage(RedirectAttributes redirectAttributes, String message) {
        redirectAttributes.addFlashAttribute("error", "Ошибка: " + message);
    }

    /**
     * Обработать исключение и добавить сообщение об ошибке
     */
    protected void handleException(RedirectAttributes redirectAttributes, Exception e) {
        addErrorMessage(redirectAttributes, e.getMessage());
    }

    /**
     * Получить ID текущего аутентифицированного пользователя
     * Если пользователь не аутентифицирован, возвращает DEFAULT_USER_ID для демо
     * @return ID пользователя
     */
    protected Long getCurrentUserId() {
        Long userId = SecurityUtil.getCurrentUserId();
        return userId != null ? userId : DEFAULT_USER_ID;
    }
}

