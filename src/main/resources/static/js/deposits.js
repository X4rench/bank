// Функции для работы со вкладами
function openCreateDepositModal() {
    openModal('createDepositModal');
}

document.getElementById('createDepositForm')?.addEventListener('submit', async function(e) {
    e.preventDefault();
    const formData = new FormData(e.target);
    const data = {
        userId: 2,
        depositType: formData.get('depositType'),
        principalAmount: formData.get('principalAmount'),
        interestRate: formData.get('interestRate'),
        termMonths: formData.get('termMonths'),
        isAutoRenewal: formData.has('isAutoRenewal'),
        currency: formData.get('currency')
    };

    try {
        await apiCall('/api/deposits', 'POST', data);
        alert('Вклад открыт успешно!');
        closeModal('createDepositModal');
        location.reload();
    } catch (error) {
        console.error('Failed to create deposit:', error);
    }
});

async function calculateInterest(depositId) {
    try {
        await apiCall(`/api/deposits/${depositId}/calculate-interest`, 'POST');
        alert('Проценты начислены!');
        location.reload();
    } catch (error) {
        console.error('Failed to calculate interest:', error);
    }
}


