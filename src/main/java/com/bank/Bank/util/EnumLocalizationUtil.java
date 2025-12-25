package com.bank.Bank.util;

import com.bank.Bank.model.enums.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Утилитный класс для локализации enum значений
 */
public final class EnumLocalizationUtil {

    private EnumLocalizationUtil() {
        // Утилитный класс - запрет создания экземпляров
    }

    /**
     * Получить локализованные названия типов переводов
     */
    public static Map<TransferType, String> getTransferTypeNames() {
        Map<TransferType, String> names = new HashMap<>();
        names.put(TransferType.INTERNAL, "Внутренний перевод");
        names.put(TransferType.EXTERNAL, "Внешний перевод");
        names.put(TransferType.SBP, "СБП");
        names.put(TransferType.P2P, "P2P");
        names.put(TransferType.SELF, "Между своими счетами");
        return names;
    }

    /**
     * Получить локализованные названия категорий платежей
     */
    public static Map<PaymentCategory, String> getPaymentCategoryNames() {
        Map<PaymentCategory, String> names = new HashMap<>();
        names.put(PaymentCategory.UTILITIES, "Коммунальные услуги");
        names.put(PaymentCategory.FOOD, "Еда");
        names.put(PaymentCategory.TRANSPORT, "Транспорт");
        names.put(PaymentCategory.ENTERTAINMENT, "Развлечения");
        names.put(PaymentCategory.SHOPPING, "Покупки");
        names.put(PaymentCategory.HEALTH, "Здоровье");
        names.put(PaymentCategory.EDUCATION, "Образование");
        names.put(PaymentCategory.BILLS, "Счета");
        names.put(PaymentCategory.TRANSFER, "Переводы");
        names.put(PaymentCategory.OTHER, "Прочее");
        return names;
    }

    /**
     * Получить локализованные названия типов карт
     */
    public static Map<CardType, String> getCardTypeNames() {
        Map<CardType, String> names = new HashMap<>();
        names.put(CardType.DEBIT, "Дебетовая");
        names.put(CardType.CREDIT, "Кредитная");
        names.put(CardType.PREPAID, "Предоплаченная");
        names.put(CardType.VIRTUAL, "Виртуальная");
        return names;
    }

    /**
     * Получить локализованные названия типов кредитов
     */
    public static Map<LoanType, String> getLoanTypeNames() {
        Map<LoanType, String> names = new HashMap<>();
        names.put(LoanType.CONSUMER, "Потребительский");
        names.put(LoanType.MORTGAGE, "Ипотека");
        names.put(LoanType.AUTO, "Автокредит");
        names.put(LoanType.CREDIT_CARD, "Кредитная карта");
        names.put(LoanType.MICROLOAN, "Микрозайм");
        return names;
    }

    /**
     * Получить локализованные названия типов вкладов
     */
    public static Map<DepositType, String> getDepositTypeNames() {
        Map<DepositType, String> names = new HashMap<>();
        names.put(DepositType.DEMAND, "До востребования");
        names.put(DepositType.TERM, "Срочный вклад");
        names.put(DepositType.SAVINGS, "Накопительный");
        names.put(DepositType.FOREIGN_CURRENCY, "Валютный");
        return names;
    }

    /**
     * Получить локализованные названия типов уведомлений
     */
    public static Map<NotificationType, String> getNotificationTypeNames() {
        Map<NotificationType, String> names = new HashMap<>();
        names.put(NotificationType.TRANSACTION, "💳 Транзакция");
        names.put(NotificationType.BALANCE_CHANGE, "💰 Изменение баланса");
        names.put(NotificationType.PAYMENT_DUE, "💸 Срок оплаты");
        names.put(NotificationType.LOAN_REMINDER, "📋 Напоминание о кредите");
        names.put(NotificationType.PROMOTION, "🎁 Акция");
        names.put(NotificationType.SECURITY, "🔒 Безопасность");
        return names;
    }
}

