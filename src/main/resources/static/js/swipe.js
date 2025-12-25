// Жесты свайпа для карт

function initSwipeableCards() {
    const cards = document.querySelectorAll('.bank-card');
    
    cards.forEach(card => {
        let startX = 0;
        let currentX = 0;
        let isDragging = false;
        
        card.addEventListener('touchstart', (e) => {
            startX = e.touches[0].clientX;
            isDragging = true;
            card.classList.add('swiping');
        });
        
        card.addEventListener('touchmove', (e) => {
            if (!isDragging) return;
            currentX = e.touches[0].clientX - startX;
            
            // Ограничиваем движение
            if (Math.abs(currentX) > 100) {
                currentX = currentX > 0 ? 100 : -100;
            }
            
            card.style.transform = `translateX(${currentX}px)`;
        });
        
        card.addEventListener('touchend', () => {
            isDragging = false;
            card.classList.remove('swiping');
            
            // Если смахнули достаточно, показываем действия
            if (Math.abs(currentX) > 50) {
                card.style.transform = `translateX(${currentX > 0 ? 100 : -100}px)`;
                setTimeout(() => {
                    card.style.transform = '';
                    showCardActions(card);
                }, 300);
            } else {
                card.style.transform = '';
            }
        });
    });
}

function showCardActions(card) {
    // Можно добавить меню действий
    console.log('Show actions for card');
}

document.addEventListener('DOMContentLoaded', initSwipeableCards);

