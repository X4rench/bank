// Функции для работы с кредитами
function openCreateLoanModal() {
    openModal('createLoanModal');
}

document.getElementById('createLoanForm')?.addEventListener('submit', async function(e) {
    e.preventDefault();
    const formData = new FormData(e.target);
    const data = {
        userId: 2,
        loanType: formData.get('loanType'),
        principalAmount: formData.get('principalAmount'),
        interestRate: formData.get('interestRate'),
        termMonths: formData.get('termMonths'),
        currency: formData.get('currency')
    };

    try {
        await apiCall('/api/loans', 'POST', data);
        alert('Кредит оформлен успешно!');
        closeModal('createLoanModal');
        location.reload();
    } catch (error) {
        console.error('Failed to create loan:', error);
    }
});

async function makePayment(loanId) {
    const amount = prompt('Введите сумму платежа:');
    if (amount && amount > 0) {
        try {
            await apiCall(`/api/loans/${loanId}/payment`, 'POST', { amount: amount });
            alert('Платеж внесен успешно!');
            location.reload();
        } catch (error) {
            console.error('Failed to make payment:', error);
        }
    }
}


