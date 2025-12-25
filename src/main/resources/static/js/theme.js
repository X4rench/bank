// Управление темой

const THEME_LIGHT = 'light';
const THEME_DARK = 'dark';

function initTheme() {
    const savedTheme = localStorage.getItem('theme') || THEME_DARK;
    applyTheme(savedTheme);
}

function toggleTheme() {
    const currentTheme = document.body.classList.contains('theme-light') ? THEME_LIGHT : THEME_DARK;
    const newTheme = currentTheme === THEME_DARK ? THEME_LIGHT : THEME_DARK;
    applyTheme(newTheme);
    localStorage.setItem('theme', newTheme);
}

function applyTheme(theme) {
    const body = document.body;
    const icon = document.getElementById('themeIcon');
    
    if (theme === THEME_LIGHT) {
        body.classList.add('theme-light');
        body.classList.remove('theme-dark');
        if (icon) icon.textContent = '🌙';
    } else {
        body.classList.add('theme-dark');
        body.classList.remove('theme-light');
        if (icon) icon.textContent = '☀️';
    }
}

document.addEventListener('DOMContentLoaded', initTheme);

