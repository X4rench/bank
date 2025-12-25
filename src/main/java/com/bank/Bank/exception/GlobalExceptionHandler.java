package com.bank.Bank.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.Map;

/**
 * Глобальный обработчик исключений для приложения
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Обработка ошибок валидации
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleValidationExceptions(
            MethodArgumentNotValidException ex,
            RedirectAttributes redirectAttributes) {
        
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        
        logger.warn("Validation errors: {}", errors);
        redirectAttributes.addFlashAttribute("errors", errors);
        redirectAttributes.addFlashAttribute("error", "Проверьте правильность заполнения полей");
        
        return "redirect:/";
    }

    /**
     * Обработка общих исключений
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleGeneralException(Exception ex, Model model, RedirectAttributes redirectAttributes) {
        logger.error("Unexpected error occurred", ex);
        
        String errorMessage = "Произошла ошибка: " + ex.getMessage();
        
        // Если используется RedirectAttributes, перенаправляем
        if (redirectAttributes != null) {
            redirectAttributes.addFlashAttribute("error", errorMessage);
            return "redirect:/";
        }
        
        // Иначе показываем страницу ошибки
        model.addAttribute("error", errorMessage);
        return "error";
    }

    /**
     * Обработка IllegalArgumentException (неверные параметры)
     */
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleIllegalArgumentException(IllegalArgumentException ex, RedirectAttributes redirectAttributes) {
        logger.warn("Illegal argument: {}", ex.getMessage());
        redirectAttributes.addFlashAttribute("error", ex.getMessage());
        return "redirect:/";
    }
}

