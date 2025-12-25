/**
 * Система toast-уведомлений для приложения
 */
class ToastManager {
    constructor() {
        this.container = null;
        this.init();
    }

    init() {
        // Создаем контейнер для toast-уведомлений
        this.container = document.createElement('div');
        this.container.className = 'toast-container';
        this.container.setAttribute('aria-live', 'polite');
        this.container.setAttribute('aria-atomic', 'true');
        document.body.appendChild(this.container);
        
        // Показываем flash-сообщения при загрузке страницы
        this.showFlashMessages();
    }

    showFlashMessages() {
        // Проверяем наличие flash-сообщений из сервера
        const urlParams = new URLSearchParams(window.location.search);
        const success = urlParams.get('success');
        const error = urlParams.get('error');
        
        if (success) {
            this.success(success);
            // Убираем параметр из URL
            window.history.replaceState({}, document.title, window.location.pathname);
        }
        
        if (error) {
            this.error(error);
            window.history.replaceState({}, document.title, window.location.pathname);
        }
    }

    /**
     * Показать успешное уведомление
     */
    success(message, duration = 4000) {
        this.show(message, 'success', duration);
    }

    /**
     * Показать ошибку
     */
    error(message, duration = 6000) {
        this.show(message, 'error', duration);
    }

    /**
     * Показать информационное уведомление
     */
    info(message, duration = 4000) {
        this.show(message, 'info', duration);
    }

    /**
     * Показать предупреждение
     */
    warning(message, duration = 5000) {
        this.show(message, 'warning', duration);
    }

    /**
     * Показать toast-уведомление
     */
    show(message, type = 'info', duration = 4000) {
        const toast = document.createElement('div');
        toast.className = `toast toast-${type}`;
        toast.setAttribute('role', 'alert');
        
        const icon = this.getIcon(type);
        toast.innerHTML = `
            <div class="toast-icon">${icon}</div>
            <div class="toast-message">${this.escapeHtml(message)}</div>
            <button class="toast-close" aria-label="Закрыть">&times;</button>
        `;
        
        this.container.appendChild(toast);
        
        // Анимация появления
        requestAnimationFrame(() => {
            toast.classList.add('toast-show');
        });
        
        // Закрытие при клике
        const closeBtn = toast.querySelector('.toast-close');
        closeBtn.addEventListener('click', () => this.remove(toast));
        
        // Автоматическое закрытие
        if (duration > 0) {
            setTimeout(() => this.remove(toast), duration);
        }
        
        return toast;
    }

    /**
     * Удалить toast
     */
    remove(toast) {
        toast.classList.remove('toast-show');
        toast.classList.add('toast-hide');
        setTimeout(() => {
            if (toast.parentNode) {
                toast.parentNode.removeChild(toast);
            }
        }, 300);
    }

    /**
     * Получить иконку для типа уведомления
     */
    getIcon(type) {
        const icons = {
            success: '✓',
            error: '✕',
            info: 'ℹ',
            warning: '⚠'
        };
        return icons[type] || icons.info;
    }

    /**
     * Экранировать HTML
     */
    escapeHtml(text) {
        const div = document.createElement('div');
        div.textContent = text;
        return div.innerHTML;
    }
}

// Инициализация при загрузке DOM
let toastManager;
document.addEventListener('DOMContentLoaded', () => {
    toastManager = new ToastManager();
    
    // Обработка flash-сообщений из Thymeleaf
    const successFlash = document.querySelector('[data-success]');
    const errorFlash = document.querySelector('[data-error]');
    
    if (successFlash) {
        toastManager.success(successFlash.getAttribute('data-success'));
    }
    
    if (errorFlash) {
        toastManager.error(errorFlash.getAttribute('data-error'));
    }
});

// Глобальная функция для использования в коде
window.showToast = {
    success: (message) => toastManager?.success(message),
    error: (message) => toastManager?.error(message),
    info: (message) => toastManager?.info(message),
    warning: (message) => toastManager?.warning(message)
};

