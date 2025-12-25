-- SQL script for initializing comprehensive test data in bank_app database
-- This script is executed after Hibernate creates the tables
-- Password for all users: password123 (encrypted with BCrypt)

-- Disable foreign key checks temporarily
SET FOREIGN_KEY_CHECKS = 0;

-- Clear existing data
DELETE FROM expenses;
DELETE FROM budgets;
DELETE FROM payment_templates;
DELETE FROM bills;
DELETE FROM transfers;
DELETE FROM deposits;
DELETE FROM loans;
DELETE FROM cards;
DELETE FROM transactions;
DELETE FROM notifications;
DELETE FROM investments;
DELETE FROM currency_exchanges;
DELETE FROM insurances;
DELETE FROM accounts;
DELETE FROM users;

-- Reset auto increment
ALTER TABLE users AUTO_INCREMENT = 1;
ALTER TABLE accounts AUTO_INCREMENT = 1;
ALTER TABLE cards AUTO_INCREMENT = 1;
ALTER TABLE transfers AUTO_INCREMENT = 1;
ALTER TABLE transactions AUTO_INCREMENT = 1;
ALTER TABLE bills AUTO_INCREMENT = 1;
ALTER TABLE loans AUTO_INCREMENT = 1;
ALTER TABLE deposits AUTO_INCREMENT = 1;
ALTER TABLE budgets AUTO_INCREMENT = 1;
ALTER TABLE notifications AUTO_INCREMENT = 1;
ALTER TABLE expenses AUTO_INCREMENT = 1;
ALTER TABLE payment_templates AUTO_INCREMENT = 1;
ALTER TABLE investments AUTO_INCREMENT = 1;
ALTER TABLE currency_exchanges AUTO_INCREMENT = 1;
ALTER TABLE insurances AUTO_INCREMENT = 1;

SET FOREIGN_KEY_CHECKS = 1;

-- Insert users
-- Password: password123 (BCrypt hash)
INSERT INTO users (id, email, password, first_name, last_name, role)
VALUES 
(1, 'admin@bank.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iwkEx5Z2O', 'Админ', 'Пользователь', 'ADMIN'),
(2, 'ivan.petrov@example.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iwkEx5Z2O', 'Иван', 'Петров', 'USER'),
(3, 'maria.sidorova@example.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iwkEx5Z2O', 'Мария', 'Сидорова', 'USER'),
(4, 'alexey.ivanov@example.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iwkEx5Z2O', 'Алексей', 'Иванов', 'USER'),
(5, 'elena.kuznetsova@example.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iwkEx5Z2O', 'Елена', 'Кузнецова', 'USER'),
(6, 'dmitry.sokolov@example.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iwkEx5Z2O', 'Дмитрий', 'Соколов', 'USER');

-- Insert accounts
INSERT INTO accounts (id, account_number, balance, user_id, account_type, currency, created_at, is_active)
VALUES 
-- User 2 (Иван Петров)
(1, '40817810099910004312', 285750.50, 2, 'CURRENT', 'RUB', DATE_SUB(NOW(), INTERVAL 2 YEAR), 1),
(2, '40817810099910004313', 150000.00, 2, 'SAVINGS', 'RUB', DATE_SUB(NOW(), INTERVAL 18 MONTH), 1),
(3, '40817810099910004314', 12500.00, 2, 'CURRENT', 'USD', DATE_SUB(NOW(), INTERVAL 1 YEAR), 1),
(4, '40817810099910004315', 8500.00, 2, 'CURRENT', 'EUR', DATE_SUB(NOW(), INTERVAL 8 MONTH), 1),
-- User 3 (Мария Сидорова)
(5, '40817810099910005312', 125000.00, 3, 'CURRENT', 'RUB', DATE_SUB(NOW(), INTERVAL 3 YEAR), 1),
(6, '40817810099910005313', 75000.00, 3, 'SAVINGS', 'RUB', DATE_SUB(NOW(), INTERVAL 2 YEAR), 1),
(7, '40817810099910005314', 3000.00, 3, 'CURRENT', 'USD', DATE_SUB(NOW(), INTERVAL 6 MONTH), 1),
-- User 4 (Алексей Иванов)
(8, '40817810099910006312', 95000.00, 4, 'CURRENT', 'RUB', DATE_SUB(NOW(), INTERVAL 1 YEAR), 1),
(9, '40817810099910006313', 45000.00, 4, 'SAVINGS', 'RUB', DATE_SUB(NOW(), INTERVAL 8 MONTH), 1),
-- User 5 (Елена Кузнецова)
(10, '40817810099910007312', 320000.00, 5, 'CURRENT', 'RUB', DATE_SUB(NOW(), INTERVAL 2 YEAR), 1),
(11, '40817810099910007313', 180000.00, 5, 'SAVINGS', 'RUB', DATE_SUB(NOW(), INTERVAL 15 MONTH), 1),
(12, '40817810099910007314', 25000.00, 5, 'CURRENT', 'EUR', DATE_SUB(NOW(), INTERVAL 1 YEAR), 1),
-- User 6 (Дмитрий Соколов)
(13, '40817810099910008312', 65000.00, 6, 'CURRENT', 'RUB', DATE_SUB(NOW(), INTERVAL 6 MONTH), 1);

-- Insert cards
INSERT INTO cards (id, card_number, cardholder_name, expiry_date, cvv, card_type, status, currency, balance, credit_limit, is_virtual, created_at, account_id)
VALUES 
-- User 2 cards
(1, '4276123456789012', 'IVAN PETROV', DATE_ADD(CURDATE(), INTERVAL 3 YEAR), '123', 'DEBIT', 'ACTIVE', 'RUB', 0.00, NULL, 0, DATE_SUB(NOW(), INTERVAL 2 YEAR), 1),
(2, '5169123456789012', 'IVAN PETROV', DATE_ADD(CURDATE(), INTERVAL 3 YEAR), '456', 'CREDIT', 'ACTIVE', 'RUB', 0.00, 300000.00, 0, DATE_SUB(NOW(), INTERVAL 18 MONTH), 1),
(3, '4276987654321098', 'IVAN PETROV', DATE_ADD(CURDATE(), INTERVAL 1 YEAR), '789', 'VIRTUAL', 'ACTIVE', 'RUB', 0.00, NULL, 1, DATE_SUB(NOW(), INTERVAL 6 MONTH), 1),
(4, '5522123456789012', 'IVAN PETROV', DATE_ADD(CURDATE(), INTERVAL 3 YEAR), '321', 'DEBIT', 'ACTIVE', 'USD', 0.00, NULL, 0, DATE_SUB(NOW(), INTERVAL 1 YEAR), 3),
(5, '4916123456789012', 'IVAN PETROV', DATE_ADD(CURDATE(), INTERVAL 3 YEAR), '654', 'DEBIT', 'ACTIVE', 'EUR', 0.00, NULL, 0, DATE_SUB(NOW(), INTERVAL 8 MONTH), 4),
-- User 3 cards
(6, '4276234567890123', 'MARIA SIDOROVA', DATE_ADD(CURDATE(), INTERVAL 3 YEAR), '111', 'DEBIT', 'ACTIVE', 'RUB', 0.00, NULL, 0, DATE_SUB(NOW(), INTERVAL 3 YEAR), 5),
(7, '5169234567890123', 'MARIA SIDOROVA', DATE_ADD(CURDATE(), INTERVAL 3 YEAR), '222', 'CREDIT', 'ACTIVE', 'RUB', 0.00, 200000.00, 0, DATE_SUB(NOW(), INTERVAL 2 YEAR), 5),
(8, '5522234567890123', 'MARIA SIDOROVA', DATE_ADD(CURDATE(), INTERVAL 2 YEAR), '333', 'DEBIT', 'ACTIVE', 'USD', 0.00, NULL, 0, DATE_SUB(NOW(), INTERVAL 6 MONTH), 7),
-- User 4 cards
(9, '4276345678901234', 'ALEXEY IVANOV', DATE_ADD(CURDATE(), INTERVAL 3 YEAR), '444', 'DEBIT', 'ACTIVE', 'RUB', 0.00, NULL, 0, DATE_SUB(NOW(), INTERVAL 1 YEAR), 8),
(10, '4276445678901234', 'ALEXEY IVANOV', DATE_ADD(CURDATE(), INTERVAL 1 YEAR), '555', 'VIRTUAL', 'ACTIVE', 'RUB', 0.00, NULL, 1, DATE_SUB(NOW(), INTERVAL 3 MONTH), 8),
-- User 5 cards
(11, '4276555678901234', 'ELENA KUZNETSOVA', DATE_ADD(CURDATE(), INTERVAL 3 YEAR), '666', 'DEBIT', 'ACTIVE', 'RUB', 0.00, NULL, 0, DATE_SUB(NOW(), INTERVAL 2 YEAR), 10),
(12, '5169555678901234', 'ELENA KUZNETSOVA', DATE_ADD(CURDATE(), INTERVAL 3 YEAR), '777', 'CREDIT', 'ACTIVE', 'RUB', 0.00, 500000.00, 0, DATE_SUB(NOW(), INTERVAL 18 MONTH), 10),
(13, '4916555678901234', 'ELENA KUZNETSOVA', DATE_ADD(CURDATE(), INTERVAL 3 YEAR), '888', 'DEBIT', 'ACTIVE', 'EUR', 0.00, NULL, 0, DATE_SUB(NOW(), INTERVAL 1 YEAR), 12),
-- User 6 cards
(14, '4276665678901234', 'DMITRY SOKOLOV', DATE_ADD(CURDATE(), INTERVAL 3 YEAR), '999', 'DEBIT', 'ACTIVE', 'RUB', 0.00, NULL, 0, DATE_SUB(NOW(), INTERVAL 6 MONTH), 13);

-- Insert loans
INSERT INTO loans (id, loan_number, loan_type, status, principal_amount, interest_rate, remaining_balance, monthly_payment, start_date, end_date, next_payment_date, currency, description, created_at, user_id)
VALUES 
-- User 2 loans
(1, 'LOAN0000001', 'MORTGAGE', 'ACTIVE', 5000000.00, 7.5, 4200000.00, 48500.00, DATE_SUB(CURDATE(), INTERVAL 18 MONTH), DATE_ADD(CURDATE(), INTERVAL 102 MONTH), DATE_ADD(CURDATE(), INTERVAL 15 DAY), 'RUB', 'Ипотечный кредит на квартиру', DATE_SUB(NOW(), INTERVAL 18 MONTH), 2),
(2, 'LOAN0000002', 'AUTO', 'ACTIVE', 1200000.00, 11.5, 950000.00, 38500.00, DATE_SUB(CURDATE(), INTERVAL 8 MONTH), DATE_ADD(CURDATE(), INTERVAL 28 MONTH), DATE_ADD(CURDATE(), INTERVAL 10 DAY), 'RUB', 'Автокредит на покупку автомобиля', DATE_SUB(NOW(), INTERVAL 8 MONTH), 2),
(3, 'LOAN0000003', 'CONSUMER', 'ACTIVE', 350000.00, 15.0, 280000.00, 22000.00, DATE_SUB(CURDATE(), INTERVAL 5 MONTH), DATE_ADD(CURDATE(), INTERVAL 13 MONTH), DATE_ADD(CURDATE(), INTERVAL 12 DAY), 'RUB', 'Потребительский кредит', DATE_SUB(NOW(), INTERVAL 5 MONTH), 2),
-- User 3 loans
(4, 'LOAN0000004', 'CONSUMER', 'ACTIVE', 250000.00, 16.5, 180000.00, 18500.00, DATE_SUB(CURDATE(), INTERVAL 6 MONTH), DATE_ADD(CURDATE(), INTERVAL 10 MONTH), DATE_ADD(CURDATE(), INTERVAL 8 DAY), 'RUB', 'Потребительский кредит на ремонт', DATE_SUB(NOW(), INTERVAL 6 MONTH), 3),
-- User 4 loans
(5, 'LOAN0000005', 'AUTO', 'ACTIVE', 800000.00, 12.0, 650000.00, 28500.00, DATE_SUB(CURDATE(), INTERVAL 4 MONTH), DATE_ADD(CURDATE(), INTERVAL 24 MONTH), DATE_ADD(CURDATE(), INTERVAL 5 DAY), 'RUB', 'Автокредит', DATE_SUB(NOW(), INTERVAL 4 MONTH), 4),
-- User 5 loans
(6, 'LOAN0000006', 'MORTGAGE', 'ACTIVE', 8000000.00, 6.8, 7200000.00, 68000.00, DATE_SUB(CURDATE(), INTERVAL 24 MONTH), DATE_ADD(CURDATE(), INTERVAL 96 MONTH), DATE_ADD(CURDATE(), INTERVAL 18 DAY), 'RUB', 'Ипотека на дом', DATE_SUB(NOW(), INTERVAL 24 MONTH), 5),
-- User 2 - paid off loan
(7, 'LOAN0000007', 'CONSUMER', 'PAID_OFF', 100000.00, 18.0, 0.00, 0.00, DATE_SUB(CURDATE(), INTERVAL 12 MONTH), DATE_SUB(CURDATE(), INTERVAL 1 MONTH), NULL, 'RUB', 'Потребительский кредит (погашен)', DATE_SUB(NOW(), INTERVAL 12 MONTH), 2);

-- Insert deposits
INSERT INTO deposits (id, deposit_number, deposit_type, principal_amount, interest_rate, current_balance, start_date, end_date, is_auto_renewal, currency, description, created_at, user_id)
VALUES 
-- User 2 deposits
(1, 'DEP00000001', 'TERM', 200000.00, 8.5, 212000.00, DATE_SUB(CURDATE(), INTERVAL 12 MONTH), DATE_ADD(CURDATE(), INTERVAL 12 MONTH), 1, 'RUB', 'Срочный вклад Премиум', DATE_SUB(NOW(), INTERVAL 12 MONTH), 2),
(2, 'DEP00000002', 'SAVINGS', 100000.00, 6.0, 105000.00, DATE_SUB(CURDATE(), INTERVAL 10 MONTH), DATE_ADD(CURDATE(), INTERVAL 14 MONTH), 1, 'RUB', 'Накопительный вклад', DATE_SUB(NOW(), INTERVAL 10 MONTH), 2),
(3, 'DEP00000003', 'FOREIGN_CURRENCY', 10000.00, 3.5, 10250.00, DATE_SUB(CURDATE(), INTERVAL 8 MONTH), DATE_ADD(CURDATE(), INTERVAL 16 MONTH), 0, 'USD', 'Валютный вклад в долларах', DATE_SUB(NOW(), INTERVAL 8 MONTH), 2),
(4, 'DEP00000004', 'DEMAND', 50000.00, 2.5, 51000.00, DATE_SUB(CURDATE(), INTERVAL 6 MONTH), NULL, 0, 'RUB', 'Вклад до востребования', DATE_SUB(NOW(), INTERVAL 6 MONTH), 2),
-- User 3 deposits
(5, 'DEP00000005', 'TERM', 150000.00, 7.8, 158700.00, DATE_SUB(CURDATE(), INTERVAL 9 MONTH), DATE_ADD(CURDATE(), INTERVAL 15 MONTH), 1, 'RUB', 'Срочный вклад', DATE_SUB(NOW(), INTERVAL 9 MONTH), 3),
(6, 'DEP00000006', 'SAVINGS', 75000.00, 5.5, 78000.00, DATE_SUB(CURDATE(), INTERVAL 7 MONTH), DATE_ADD(CURDATE(), INTERVAL 17 MONTH), 1, 'RUB', 'Накопительный вклад', DATE_SUB(NOW(), INTERVAL 7 MONTH), 3),
-- User 4 deposits
(7, 'DEP00000007', 'TERM', 100000.00, 8.0, 106000.00, DATE_SUB(CURDATE(), INTERVAL 9 MONTH), DATE_ADD(CURDATE(), INTERVAL 15 MONTH), 1, 'RUB', 'Срочный вклад', DATE_SUB(NOW(), INTERVAL 9 MONTH), 4),
-- User 5 deposits
(8, 'DEP00000008', 'TERM', 300000.00, 8.2, 324600.00, DATE_SUB(CURDATE(), INTERVAL 12 MONTH), DATE_ADD(CURDATE(), INTERVAL 12 MONTH), 1, 'RUB', 'Срочный вклад Премиум', DATE_SUB(NOW(), INTERVAL 12 MONTH), 5),
(9, 'DEP00000009', 'FOREIGN_CURRENCY', 20000.00, 3.0, 20400.00, DATE_SUB(CURDATE(), INTERVAL 8 MONTH), DATE_ADD(CURDATE(), INTERVAL 16 MONTH), 0, 'EUR', 'Валютный вклад в евро', DATE_SUB(NOW(), INTERVAL 8 MONTH), 5);

-- Insert transfers (recent transactions)
INSERT INTO transfers (id, transfer_number, amount, transfer_type, status, currency, recipient_name, recipient_account, recipient_card, recipient_phone, description, fee, created_at, completed_at, from_account_id, to_account_id)
VALUES 
-- User 2 transfers (last 30 days)
(1, 'TRF000000001', 50000.00, 'SELF', 'COMPLETED', 'RUB', NULL, '40817810099910004313', NULL, NULL, 'Перевод на накопительный счет', 0.00, DATE_SUB(NOW(), INTERVAL 25 DAY), DATE_SUB(NOW(), INTERVAL 25 DAY), 1, 2),
(2, 'TRF000000002', 10000.00, 'INTERNAL', 'COMPLETED', 'RUB', 'Мария Сидорова', '40817810099910005312', NULL, NULL, 'Перевод другу на день рождения', 0.00, DATE_SUB(NOW(), INTERVAL 20 DAY), DATE_SUB(NOW(), INTERVAL 20 DAY), 1, 5),
(3, 'TRF000000003', 25000.00, 'EXTERNAL', 'COMPLETED', 'RUB', 'ООО "Строительство Плюс"', NULL, '5555123456789012', NULL, 'Оплата услуг по ремонту', 250.00, DATE_SUB(NOW(), INTERVAL 18 DAY), DATE_SUB(NOW(), INTERVAL 18 DAY), 1, NULL),
(4, 'TRF000000004', 5000.00, 'SBP', 'COMPLETED', 'RUB', NULL, NULL, NULL, '+79001234567', 'Перевод на телефон через СБП', 0.50, DATE_SUB(NOW(), INTERVAL 15 DAY), DATE_SUB(NOW(), INTERVAL 15 DAY), 1, NULL),
(5, 'TRF000000005', 15000.00, 'P2P', 'COMPLETED', 'RUB', 'Иван Смирнов', NULL, NULL, '+79009876543', 'P2P перевод', 150.00, DATE_SUB(NOW(), INTERVAL 12 DAY), DATE_SUB(NOW(), INTERVAL 12 DAY), 1, NULL),
(6, 'TRF000000006', 2000.00, 'SBP', 'COMPLETED', 'RUB', NULL, NULL, NULL, '+79005556677', 'Оплата такси', 0.50, DATE_SUB(NOW(), INTERVAL 10 DAY), DATE_SUB(NOW(), INTERVAL 10 DAY), 1, NULL),
(7, 'TRF000000007', 30000.00, 'SELF', 'COMPLETED', 'RUB', NULL, '40817810099910004314', NULL, NULL, 'Конвертация в USD', 0.00, DATE_SUB(NOW(), INTERVAL 8 DAY), DATE_SUB(NOW(), INTERVAL 8 DAY), 1, 3),
(8, 'TRF000000008', 50000.00, 'EXTERNAL', 'COMPLETED', 'RUB', 'ИП Петров', NULL, '5555987654321098', NULL, 'Оплата товаров', 500.00, DATE_SUB(NOW(), INTERVAL 5 DAY), DATE_SUB(NOW(), INTERVAL 5 DAY), 1, NULL),
(9, 'TRF000000009', 1000.00, 'SBP', 'COMPLETED', 'RUB', NULL, NULL, NULL, '+79001112233', 'Мелкий перевод', 0.50, DATE_SUB(NOW(), INTERVAL 3 DAY), DATE_SUB(NOW(), INTERVAL 3 DAY), 1, NULL),
(10, 'TRF000000010', 75000.00, 'INTERNAL', 'COMPLETED', 'RUB', 'Алексей Иванов', '40817810099910006312', NULL, NULL, 'Возврат долга', 0.00, DATE_SUB(NOW(), INTERVAL 2 DAY), DATE_SUB(NOW(), INTERVAL 2 DAY), 1, 8),
-- User 3 transfers
(11, 'TRF000000011', 25000.00, 'EXTERNAL', 'COMPLETED', 'RUB', 'ООО "Ремонт Сервис"', NULL, '5555234567890123', NULL, 'Оплата ремонта', 250.00, DATE_SUB(NOW(), INTERVAL 14 DAY), DATE_SUB(NOW(), INTERVAL 14 DAY), 5, NULL),
(12, 'TRF000000012', 15000.00, 'SELF', 'COMPLETED', 'RUB', NULL, '40817810099910005313', NULL, NULL, 'Накопления', 0.00, DATE_SUB(NOW(), INTERVAL 7 DAY), DATE_SUB(NOW(), INTERVAL 7 DAY), 5, 6),
-- User 4 transfers
(13, 'TRF000000013', 10000.00, 'INTERNAL', 'COMPLETED', 'RUB', 'Иван Петров', '40817810099910004312', NULL, NULL, 'Оплата за ужин', 0.00, DATE_SUB(NOW(), INTERVAL 6 DAY), DATE_SUB(NOW(), INTERVAL 6 DAY), 8, 1),
-- User 5 transfers
(14, 'TRF000000014', 100000.00, 'EXTERNAL', 'COMPLETED', 'RUB', 'ООО "Инвестиции"', NULL, '5555345678901234', NULL, 'Инвестиции', 1000.00, DATE_SUB(NOW(), INTERVAL 16 DAY), DATE_SUB(NOW(), INTERVAL 16 DAY), 10, NULL),
(15, 'TRF000000015', 50000.00, 'SELF', 'COMPLETED', 'RUB', NULL, '40817810099910007313', NULL, NULL, 'Накопительный перевод', 0.00, DATE_SUB(NOW(), INTERVAL 9 DAY), DATE_SUB(NOW(), INTERVAL 9 DAY), 10, 11);

-- Insert bills
INSERT INTO bills (id, bill_number, provider_name, amount, status, category, account_number, payment_code, due_date, paid_date, description, is_auto_payment, auto_payment_date, created_at, account_id)
VALUES 
-- User 2 bills
(1, 'BILL0000001', 'Мосэнергосбыт', 4500.00, 'PENDING', 'UTILITIES', '123456789012', '12345', DATE_ADD(CURDATE(), INTERVAL 8 DAY), NULL, 'Оплата электроэнергии', 1, DATE_ADD(CURDATE(), INTERVAL 8 DAY), DATE_SUB(NOW(), INTERVAL 15 DAY), 1),
(2, 'BILL0000002', 'Водоканал', 2800.00, 'PENDING', 'UTILITIES', '987654321098', '67890', DATE_ADD(CURDATE(), INTERVAL 12 DAY), NULL, 'Оплата водоснабжения', 1, DATE_ADD(CURDATE(), INTERVAL 12 DAY), DATE_SUB(NOW(), INTERVAL 12 DAY), 1),
(3, 'BILL0000003', 'Ростелеком', 890.00, 'PENDING', 'BILLS', '111111111111', '11111', DATE_ADD(CURDATE(), INTERVAL 5 DAY), NULL, 'Интернет и ТВ', 1, DATE_ADD(CURDATE(), INTERVAL 5 DAY), DATE_SUB(NOW(), INTERVAL 20 DAY), 1),
(4, 'BILL0000004', 'МТС', 550.00, 'COMPLETED', 'BILLS', '222222222222', '22222', DATE_SUB(CURDATE(), INTERVAL 3 DAY), DATE_SUB(CURDATE(), INTERVAL 3 DAY), 'Мобильная связь', 1, DATE_ADD(CURDATE(), INTERVAL 27 DAY), DATE_SUB(NOW(), INTERVAL 30 DAY), 1),
(5, 'BILL0000005', 'МегаФон', 450.00, 'COMPLETED', 'BILLS', '333333333333', '33333', DATE_SUB(CURDATE(), INTERVAL 10 DAY), DATE_SUB(CURDATE(), INTERVAL 10 DAY), 'Мобильная связь (дополнительный номер)', 0, NULL, DATE_SUB(NOW(), INTERVAL 35 DAY), 1),
(6, 'BILL0000006', 'ГИБДД', 2500.00, 'PENDING', 'BILLS', '444444444444', '44444', DATE_ADD(CURDATE(), INTERVAL 15 DAY), NULL, 'Штраф за превышение скорости', 0, NULL, DATE_SUB(NOW(), INTERVAL 7 DAY), 1),
(7, 'BILL0000007', 'ФНС', 15000.00, 'PENDING', 'BILLS', '555555555555', '55555', DATE_ADD(CURDATE(), INTERVAL 20 DAY), NULL, 'Налог на имущество', 0, NULL, DATE_SUB(NOW(), INTERVAL 5 DAY), 1),
(8, 'BILL0000008', 'Детский сад №123', 8500.00, 'COMPLETED', 'EDUCATION', '666666666666', '66666', DATE_SUB(CURDATE(), INTERVAL 5 DAY), DATE_SUB(CURDATE(), INTERVAL 5 DAY), 'Оплата детского сада', 1, DATE_ADD(CURDATE(), INTERVAL 25 DAY), DATE_SUB(NOW(), INTERVAL 30 DAY), 1),
(9, 'BILL0000009', 'Спортзал "Фитнес"', 3000.00, 'PENDING', 'HEALTH', '777777777777', '77777', DATE_ADD(CURDATE(), INTERVAL 10 DAY), NULL, 'Абонемент в спортзал', 1, DATE_ADD(CURDATE(), INTERVAL 10 DAY), DATE_SUB(NOW(), INTERVAL 25 DAY), 1),
(10, 'BILL0000010', 'СберСтрахование', 8500.00, 'COMPLETED', 'BILLS', '888888888888', '88888', DATE_SUB(CURDATE(), INTERVAL 2 DAY), DATE_SUB(CURDATE(), INTERVAL 2 DAY), 'Страхование ОСАГО', 1, DATE_ADD(CURDATE(), INTERVAL 28 DAY), DATE_SUB(NOW(), INTERVAL 32 DAY), 1),
-- User 3 bills
(11, 'BILL0000011', 'Мосэнергосбыт', 3200.00, 'PENDING', 'UTILITIES', '999999999999', '99999', DATE_ADD(CURDATE(), INTERVAL 7 DAY), NULL, 'Оплата электроэнергии', 1, DATE_ADD(CURDATE(), INTERVAL 7 DAY), DATE_SUB(NOW(), INTERVAL 18 DAY), 5),
(12, 'BILL0000012', 'Билайн', 500.00, 'COMPLETED', 'BILLS', '101010101010', '10101', DATE_SUB(CURDATE(), INTERVAL 5 DAY), DATE_SUB(CURDATE(), INTERVAL 5 DAY), 'Мобильная связь', 1, DATE_ADD(CURDATE(), INTERVAL 25 DAY), DATE_SUB(NOW(), INTERVAL 28 DAY), 5),
-- User 4 bills
(13, 'BILL0000013', 'Мосэнергосбыт', 2800.00, 'PENDING', 'UTILITIES', '202020202020', '20202', DATE_ADD(CURDATE(), INTERVAL 6 DAY), NULL, 'Оплата электроэнергии', 1, DATE_ADD(CURDATE(), INTERVAL 6 DAY), DATE_SUB(NOW(), INTERVAL 22 DAY), 8),
-- User 5 bills
(14, 'BILL0000014', 'Мосэнергосбыт', 6500.00, 'PENDING', 'UTILITIES', '303030303030', '30303', DATE_ADD(CURDATE(), INTERVAL 9 DAY), NULL, 'Оплата электроэнергии', 1, DATE_ADD(CURDATE(), INTERVAL 9 DAY), DATE_SUB(NOW(), INTERVAL 16 DAY), 10),
(15, 'BILL0000015', 'Теле2', 700.00, 'PENDING', 'BILLS', '404040404040', '40404', DATE_ADD(CURDATE(), INTERVAL 4 DAY), NULL, 'Мобильная связь', 1, DATE_ADD(CURDATE(), INTERVAL 4 DAY), DATE_SUB(NOW(), INTERVAL 24 DAY), 10);

-- Insert payment templates
INSERT INTO payment_templates (id, template_name, provider_name, amount, category, account_number, payment_code, description, is_recurring, recurring_day, created_at, account_id)
VALUES 
(1, 'Ежемесячная оплата ЖКХ', 'Мосэнергосбыт', 4500.00, 'UTILITIES', '123456789012', '12345', 'Автоматическая оплата электроэнергии', 1, 8, DATE_SUB(NOW(), INTERVAL 90 DAY), 1),
(2, 'Водоканал', 'Водоканал', 2800.00, 'UTILITIES', '987654321098', '67890', 'Ежемесячная оплата водоснабжения', 1, 12, DATE_SUB(NOW(), INTERVAL 85 DAY), 1),
(3, 'Интернет Ростелеком', 'Ростелеком', 890.00, 'BILLS', '111111111111', '11111', 'Ежемесячная оплата интернета и ТВ', 1, 5, DATE_SUB(NOW(), INTERVAL 80 DAY), 1),
(4, 'МТС мобильная связь', 'МТС', 550.00, 'BILLS', '222222222222', '22222', 'Автопополнение баланса', 1, 27, DATE_SUB(NOW(), INTERVAL 75 DAY), 1),
(5, 'Детский сад', 'Детский сад №123', 8500.00, 'EDUCATION', '666666666666', '66666', 'Ежемесячная оплата детского сада', 1, 25, DATE_SUB(NOW(), INTERVAL 70 DAY), 1),
(6, 'Спортзал', 'Спортзал "Фитнес"', 3000.00, 'HEALTH', '777777777777', '77777', 'Ежемесячный абонемент', 1, 10, DATE_SUB(NOW(), INTERVAL 65 DAY), 1),
(7, 'Страхование ОСАГО', 'СберСтрахование', 8500.00, 'BILLS', '888888888888', '88888', 'Ежемесячный платеж по ОСАГО', 1, 28, DATE_SUB(NOW(), INTERVAL 60 DAY), 1);

-- Insert budgets
INSERT INTO budgets (id, category, limit_amount, start_date, end_date, spent_amount, is_active, created_at, user_id)
VALUES 
-- User 2 budgets
(1, 'FOOD', 20000.00, DATE_SUB(CURDATE(), INTERVAL 20 DAY), DATE_ADD(CURDATE(), INTERVAL 10 DAY), 15200.00, 1, DATE_SUB(NOW(), INTERVAL 20 DAY), 2),
(2, 'TRANSPORT', 8000.00, DATE_SUB(CURDATE(), INTERVAL 20 DAY), DATE_ADD(CURDATE(), INTERVAL 10 DAY), 5800.00, 1, DATE_SUB(NOW(), INTERVAL 20 DAY), 2),
(3, 'ENTERTAINMENT', 15000.00, DATE_SUB(CURDATE(), INTERVAL 20 DAY), DATE_ADD(CURDATE(), INTERVAL 10 DAY), 11200.00, 1, DATE_SUB(NOW(), INTERVAL 20 DAY), 2),
(4, 'UTILITIES', 12000.00, DATE_SUB(CURDATE(), INTERVAL 20 DAY), DATE_ADD(CURDATE(), INTERVAL 10 DAY), 8200.00, 1, DATE_SUB(NOW(), INTERVAL 20 DAY), 2),
(5, 'SHOPPING', 30000.00, DATE_SUB(CURDATE(), INTERVAL 20 DAY), DATE_ADD(CURDATE(), INTERVAL 10 DAY), 18500.00, 1, DATE_SUB(NOW(), INTERVAL 20 DAY), 2),
(6, 'HEALTH', 10000.00, DATE_SUB(CURDATE(), INTERVAL 20 DAY), DATE_ADD(CURDATE(), INTERVAL 10 DAY), 6200.00, 1, DATE_SUB(NOW(), INTERVAL 20 DAY), 2),
(7, 'EDUCATION', 15000.00, DATE_SUB(CURDATE(), INTERVAL 20 DAY), DATE_ADD(CURDATE(), INTERVAL 10 DAY), 8500.00, 1, DATE_SUB(NOW(), INTERVAL 20 DAY), 2),
-- User 3 budgets
(8, 'FOOD', 15000.00, DATE_SUB(CURDATE(), INTERVAL 15 DAY), DATE_ADD(CURDATE(), INTERVAL 15 DAY), 9800.00, 1, DATE_SUB(NOW(), INTERVAL 15 DAY), 3),
(9, 'TRANSPORT', 6000.00, DATE_SUB(CURDATE(), INTERVAL 15 DAY), DATE_ADD(CURDATE(), INTERVAL 15 DAY), 4200.00, 1, DATE_SUB(NOW(), INTERVAL 15 DAY), 3),
(10, 'ENTERTAINMENT', 12000.00, DATE_SUB(CURDATE(), INTERVAL 15 DAY), DATE_ADD(CURDATE(), INTERVAL 15 DAY), 7500.00, 1, DATE_SUB(NOW(), INTERVAL 15 DAY), 3),
-- User 4 budgets
(11, 'FOOD', 12000.00, DATE_SUB(CURDATE(), INTERVAL 10 DAY), DATE_ADD(CURDATE(), INTERVAL 20 DAY), 6800.00, 1, DATE_SUB(NOW(), INTERVAL 10 DAY), 4),
(12, 'SHOPPING', 25000.00, DATE_SUB(CURDATE(), INTERVAL 10 DAY), DATE_ADD(CURDATE(), INTERVAL 20 DAY), 15200.00, 1, DATE_SUB(NOW(), INTERVAL 10 DAY), 4),
-- User 5 budgets
(13, 'FOOD', 35000.00, DATE_SUB(CURDATE(), INTERVAL 18 DAY), DATE_ADD(CURDATE(), INTERVAL 12 DAY), 24200.00, 1, DATE_SUB(NOW(), INTERVAL 18 DAY), 5),
(14, 'TRANSPORT', 15000.00, DATE_SUB(CURDATE(), INTERVAL 18 DAY), DATE_ADD(CURDATE(), INTERVAL 12 DAY), 10800.00, 1, DATE_SUB(NOW(), INTERVAL 18 DAY), 5),
(15, 'UTILITIES', 25000.00, DATE_SUB(CURDATE(), INTERVAL 18 DAY), DATE_ADD(CURDATE(), INTERVAL 12 DAY), 18200.00, 1, DATE_SUB(NOW(), INTERVAL 18 DAY), 5);

-- Insert expenses
INSERT INTO expenses (id, amount, category, description, expense_date, created_at, account_id)
VALUES 
-- User 2 expenses (last 30 days)
(1, 3200.00, 'FOOD', 'Продукты в супермаркете', DATE_SUB(CURDATE(), INTERVAL 28 DAY), DATE_SUB(NOW(), INTERVAL 28 DAY), 1),
(2, 1800.00, 'TRANSPORT', 'Такси и каршеринг', DATE_SUB(CURDATE(), INTERVAL 27 DAY), DATE_SUB(NOW(), INTERVAL 27 DAY), 1),
(3, 4500.00, 'ENTERTAINMENT', 'Ресторан с друзьями', DATE_SUB(CURDATE(), INTERVAL 26 DAY), DATE_SUB(NOW(), INTERVAL 26 DAY), 1),
(4, 2500.00, 'FOOD', 'Продукты и напитки', DATE_SUB(CURDATE(), INTERVAL 25 DAY), DATE_SUB(NOW(), INTERVAL 25 DAY), 1),
(5, 1200.00, 'TRANSPORT', 'Метро и автобус', DATE_SUB(CURDATE(), INTERVAL 24 DAY), DATE_SUB(NOW(), INTERVAL 24 DAY), 1),
(6, 8500.00, 'SHOPPING', 'Покупка одежды', DATE_SUB(CURDATE(), INTERVAL 23 DAY), DATE_SUB(NOW(), INTERVAL 23 DAY), 1),
(7, 3200.00, 'FOOD', 'Супермаркет', DATE_SUB(CURDATE(), INTERVAL 22 DAY), DATE_SUB(NOW(), INTERVAL 22 DAY), 1),
(8, 4500.00, 'UTILITIES', 'Коммунальные услуги', DATE_SUB(CURDATE(), INTERVAL 21 DAY), DATE_SUB(NOW(), INTERVAL 21 DAY), 1),
(9, 2500.00, 'HEALTH', 'Аптека и медицинские услуги', DATE_SUB(CURDATE(), INTERVAL 20 DAY), DATE_SUB(NOW(), INTERVAL 20 DAY), 1),
(10, 5500.00, 'ENTERTAINMENT', 'Кинотеатр и развлечения', DATE_SUB(CURDATE(), INTERVAL 19 DAY), DATE_SUB(NOW(), INTERVAL 19 DAY), 1),
(11, 2800.00, 'FOOD', 'Продукты', DATE_SUB(CURDATE(), INTERVAL 18 DAY), DATE_SUB(NOW(), INTERVAL 18 DAY), 1),
(12, 1500.00, 'TRANSPORT', 'Такси', DATE_SUB(CURDATE(), INTERVAL 17 DAY), DATE_SUB(NOW(), INTERVAL 17 DAY), 1),
(13, 12000.00, 'SHOPPING', 'Покупка электроники', DATE_SUB(CURDATE(), INTERVAL 16 DAY), DATE_SUB(NOW(), INTERVAL 16 DAY), 1),
(14, 3500.00, 'FOOD', 'Продукты и ресторан', DATE_SUB(CURDATE(), INTERVAL 15 DAY), DATE_SUB(NOW(), INTERVAL 15 DAY), 1),
(15, 8500.00, 'EDUCATION', 'Оплата детского сада', DATE_SUB(CURDATE(), INTERVAL 14 DAY), DATE_SUB(NOW(), INTERVAL 14 DAY), 1),
(16, 2800.00, 'FOOD', 'Продукты', DATE_SUB(CURDATE(), INTERVAL 13 DAY), DATE_SUB(NOW(), INTERVAL 13 DAY), 1),
(17, 2200.00, 'TRANSPORT', 'Каршеринг', DATE_SUB(CURDATE(), INTERVAL 12 DAY), DATE_SUB(NOW(), INTERVAL 12 DAY), 1),
(18, 5500.00, 'ENTERTAINMENT', 'Развлечения', DATE_SUB(CURDATE(), INTERVAL 11 DAY), DATE_SUB(NOW(), INTERVAL 11 DAY), 1),
(19, 3200.00, 'FOOD', 'Супермаркет', DATE_SUB(CURDATE(), INTERVAL 10 DAY), DATE_SUB(NOW(), INTERVAL 10 DAY), 1),
(20, 3700.00, 'UTILITIES', 'Коммунальные услуги', DATE_SUB(CURDATE(), INTERVAL 9 DAY), DATE_SUB(NOW(), INTERVAL 9 DAY), 1),
(21, 2800.00, 'FOOD', 'Продукты', DATE_SUB(CURDATE(), INTERVAL 8 DAY), DATE_SUB(NOW(), INTERVAL 8 DAY), 1),
(22, 1200.00, 'TRANSPORT', 'Метро', DATE_SUB(CURDATE(), INTERVAL 7 DAY), DATE_SUB(NOW(), INTERVAL 7 DAY), 1),
(23, 6500.00, 'SHOPPING', 'Покупки', DATE_SUB(CURDATE(), INTERVAL 6 DAY), DATE_SUB(NOW(), INTERVAL 6 DAY), 1),
(24, 3200.00, 'FOOD', 'Продукты', DATE_SUB(CURDATE(), INTERVAL 5 DAY), DATE_SUB(NOW(), INTERVAL 5 DAY), 1),
(25, 3000.00, 'HEALTH', 'Спортзал', DATE_SUB(CURDATE(), INTERVAL 4 DAY), DATE_SUB(NOW(), INTERVAL 4 DAY), 1),
(26, 2800.00, 'FOOD', 'Продукты', DATE_SUB(CURDATE(), INTERVAL 3 DAY), DATE_SUB(NOW(), INTERVAL 3 DAY), 1),
(27, 1500.00, 'TRANSPORT', 'Такси', DATE_SUB(CURDATE(), INTERVAL 2 DAY), DATE_SUB(NOW(), INTERVAL 2 DAY), 1),
(28, 4500.00, 'ENTERTAINMENT', 'Ресторан', DATE_SUB(CURDATE(), INTERVAL 1 DAY), DATE_SUB(NOW(), INTERVAL 1 DAY), 1),
(29, 3200.00, 'FOOD', 'Продукты', CURDATE(), NOW(), 1),
-- User 3 expenses
(30, 2500.00, 'FOOD', 'Продукты', DATE_SUB(CURDATE(), INTERVAL 20 DAY), DATE_SUB(NOW(), INTERVAL 20 DAY), 5),
(31, 1200.00, 'TRANSPORT', 'Метро', DATE_SUB(CURDATE(), INTERVAL 18 DAY), DATE_SUB(NOW(), INTERVAL 18 DAY), 5),
(32, 3500.00, 'ENTERTAINMENT', 'Развлечения', DATE_SUB(CURDATE(), INTERVAL 15 DAY), DATE_SUB(NOW(), INTERVAL 15 DAY), 5),
(33, 2800.00, 'FOOD', 'Продукты', DATE_SUB(CURDATE(), INTERVAL 12 DAY), DATE_SUB(NOW(), INTERVAL 12 DAY), 5),
(34, 5500.00, 'SHOPPING', 'Покупки', DATE_SUB(CURDATE(), INTERVAL 10 DAY), DATE_SUB(NOW(), INTERVAL 10 DAY), 5),
(35, 2500.00, 'FOOD', 'Продукты', DATE_SUB(CURDATE(), INTERVAL 8 DAY), DATE_SUB(NOW(), INTERVAL 8 DAY), 5),
(36, 1200.00, 'TRANSPORT', 'Такси', DATE_SUB(CURDATE(), INTERVAL 5 DAY), DATE_SUB(NOW(), INTERVAL 5 DAY), 5),
(37, 3200.00, 'FOOD', 'Продукты', DATE_SUB(CURDATE(), INTERVAL 3 DAY), DATE_SUB(NOW(), INTERVAL 3 DAY), 5),
-- User 4 expenses
(38, 2200.00, 'FOOD', 'Продукты', DATE_SUB(CURDATE(), INTERVAL 15 DAY), DATE_SUB(NOW(), INTERVAL 15 DAY), 8),
(39, 8500.00, 'SHOPPING', 'Покупки', DATE_SUB(CURDATE(), INTERVAL 12 DAY), DATE_SUB(NOW(), INTERVAL 12 DAY), 8),
(40, 2200.00, 'FOOD', 'Продукты', DATE_SUB(CURDATE(), INTERVAL 9 DAY), DATE_SUB(NOW(), INTERVAL 9 DAY), 8),
(41, 1200.00, 'TRANSPORT', 'Метро', DATE_SUB(CURDATE(), INTERVAL 6 DAY), DATE_SUB(NOW(), INTERVAL 6 DAY), 8),
(42, 2200.00, 'FOOD', 'Продукты', DATE_SUB(CURDATE(), INTERVAL 3 DAY), DATE_SUB(NOW(), INTERVAL 3 DAY), 8),
-- User 5 expenses
(43, 5500.00, 'FOOD', 'Продукты', DATE_SUB(CURDATE(), INTERVAL 25 DAY), DATE_SUB(NOW(), INTERVAL 25 DAY), 10),
(44, 3200.00, 'TRANSPORT', 'Такси и каршеринг', DATE_SUB(CURDATE(), INTERVAL 22 DAY), DATE_SUB(NOW(), INTERVAL 22 DAY), 10),
(45, 12000.00, 'SHOPPING', 'Покупки', DATE_SUB(CURDATE(), INTERVAL 20 DAY), DATE_SUB(NOW(), INTERVAL 20 DAY), 10),
(46, 5500.00, 'FOOD', 'Ресторан', DATE_SUB(CURDATE(), INTERVAL 18 DAY), DATE_SUB(NOW(), INTERVAL 18 DAY), 10),
(47, 6500.00, 'UTILITIES', 'Коммунальные услуги', DATE_SUB(CURDATE(), INTERVAL 16 DAY), DATE_SUB(NOW(), INTERVAL 16 DAY), 10),
(48, 5500.00, 'FOOD', 'Продукты', DATE_SUB(CURDATE(), INTERVAL 14 DAY), DATE_SUB(NOW(), INTERVAL 14 DAY), 10),
(49, 2800.00, 'TRANSPORT', 'Такси', DATE_SUB(CURDATE(), INTERVAL 12 DAY), DATE_SUB(NOW(), INTERVAL 12 DAY), 10),
(50, 8500.00, 'ENTERTAINMENT', 'Развлечения', DATE_SUB(CURDATE(), INTERVAL 10 DAY), DATE_SUB(NOW(), INTERVAL 10 DAY), 10),
(51, 5500.00, 'FOOD', 'Продукты', DATE_SUB(CURDATE(), INTERVAL 8 DAY), DATE_SUB(NOW(), INTERVAL 8 DAY), 10),
(52, 12000.00, 'SHOPPING', 'Покупки', DATE_SUB(CURDATE(), INTERVAL 6 DAY), DATE_SUB(NOW(), INTERVAL 6 DAY), 10),
(53, 5500.00, 'FOOD', 'Ресторан', DATE_SUB(CURDATE(), INTERVAL 4 DAY), DATE_SUB(NOW(), INTERVAL 4 DAY), 10),
(54, 3200.00, 'TRANSPORT', 'Каршеринг', DATE_SUB(CURDATE(), INTERVAL 2 DAY), DATE_SUB(NOW(), INTERVAL 2 DAY), 10);

-- Insert transactions
INSERT INTO transactions (id, amount, timestamp, type, account_id, category, description, recipient_name, recipient_account)
VALUES 
-- User 2 transactions (income)
(1, 85000.00, DATE_SUB(NOW(), INTERVAL 2 DAY), 'CREDIT', 1, 'TRANSFER', 'Зарплата', 'ООО "Работодатель"', 'ACC999'),
(2, 50000.00, DATE_SUB(NOW(), INTERVAL 32 DAY), 'CREDIT', 1, 'TRANSFER', 'Зарплата', 'ООО "Работодатель"', 'ACC999'),
(3, 50000.00, DATE_SUB(NOW(), INTERVAL 62 DAY), 'CREDIT', 1, 'TRANSFER', 'Зарплата', 'ООО "Работодатель"', 'ACC999'),
(4, 15000.00, DATE_SUB(NOW(), INTERVAL 15 DAY), 'CREDIT', 1, 'TRANSFER', 'Премия', 'ООО "Работодатель"', 'ACC999'),
(5, 25000.00, DATE_SUB(NOW(), INTERVAL 28 DAY), 'CREDIT', 1, 'TRANSFER', 'Перевод от друга', 'Иван Смирнов', 'ACC888'),
-- User 3 transactions
(6, 65000.00, DATE_SUB(NOW(), INTERVAL 5 DAY), 'CREDIT', 5, 'TRANSFER', 'Зарплата', 'ООО "Компания"', 'ACC777'),
(7, 65000.00, DATE_SUB(NOW(), INTERVAL 35 DAY), 'CREDIT', 5, 'TRANSFER', 'Зарплата', 'ООО "Компания"', 'ACC777'),
-- User 4 transactions
(8, 55000.00, DATE_SUB(NOW(), INTERVAL 3 DAY), 'CREDIT', 8, 'TRANSFER', 'Зарплата', 'ООО "Организация"', 'ACC666'),
-- User 5 transactions
(9, 120000.00, DATE_SUB(NOW(), INTERVAL 4 DAY), 'CREDIT', 10, 'TRANSFER', 'Зарплата', 'ООО "Корпорация"', 'ACC555'),
(10, 120000.00, DATE_SUB(NOW(), INTERVAL 34 DAY), 'CREDIT', 10, 'TRANSFER', 'Зарплата', 'ООО "Корпорация"', 'ACC555');

-- Insert notifications
INSERT INTO notifications (id, notification_type, title, message, is_read, created_at, user_id)
VALUES 
-- User 2 notifications
(1, 'TRANSACTION', 'Перевод выполнен', 'Перевод на сумму 75 000.00 RUB успешно выполнен', 0, DATE_SUB(NOW(), INTERVAL 2 HOUR), 2),
(2, 'BALANCE_CHANGE', 'Изменение баланса', 'Баланс счета 40817810099910004312 изменился на -75 000.00 RUB', 0, DATE_SUB(NOW(), INTERVAL 2 HOUR), 2),
(3, 'PAYMENT_DUE', 'Напоминание об оплате', 'Скоро наступит срок оплаты счета BILL0000001 (Мосэнергосбыт). Сумма: 4 500.00 RUB', 0, DATE_SUB(NOW(), INTERVAL 1 DAY), 2),
(4, 'LOAN_REMINDER', 'Напоминание о кредите', 'Не забудьте внести платеж по кредиту LOAN0000001 до 15 числа. Сумма платежа: 48 500.00 RUB', 0, DATE_SUB(NOW(), INTERVAL 2 DAY), 2),
(5, 'PROMOTION', 'Специальное предложение', 'Выгодные условия для нового депозита! Ставка до 8.5% годовых. Откройте вклад прямо сейчас!', 1, DATE_SUB(NOW(), INTERVAL 5 DAY), 2),
(6, 'SECURITY', 'Вход в систему', 'Выполнен вход в банковское приложение с нового устройства', 1, DATE_SUB(NOW(), INTERVAL 7 DAY), 2),
(7, 'TRANSACTION', 'Платеж выполнен', 'Счет BILL0000004 (МТС) успешно оплачен. Сумма: 550.00 RUB', 1, DATE_SUB(NOW(), INTERVAL 10 DAY), 2),
(8, 'BALANCE_CHANGE', 'Изменение баланса', 'Баланс счета 40817810099910004312 изменился на +85 000.00 RUB', 1, DATE_SUB(NOW(), INTERVAL 12 DAY), 2),
(9, 'TRANSACTION', 'Перевод выполнен', 'Перевод на сумму 50 000.00 RUB успешно выполнен', 1, DATE_SUB(NOW(), INTERVAL 15 DAY), 2),
(10, 'PROMOTION', 'Акция на карты', 'Оформите кредитную карту с кешбэком до 10%! Условия акции действуют ограниченное время.', 1, DATE_SUB(NOW(), INTERVAL 20 DAY), 2),
-- User 3 notifications
(11, 'TRANSACTION', 'Перевод выполнен', 'Перевод на сумму 15 000.00 RUB успешно выполнен', 0, DATE_SUB(NOW(), INTERVAL 1 HOUR), 3),
(12, 'BALANCE_CHANGE', 'Изменение баланса', 'Баланс счета 40817810099910005312 изменился на +65 000.00 RUB', 1, DATE_SUB(NOW(), INTERVAL 5 DAY), 3),
(13, 'PAYMENT_DUE', 'Напоминание об оплате', 'Скоро наступит срок оплаты счета BILL0000011 (Мосэнергосбыт). Сумма: 3 200.00 RUB', 0, DATE_SUB(NOW(), INTERVAL 3 DAY), 3),
-- User 4 notifications
(14, 'TRANSACTION', 'Перевод выполнен', 'Перевод на сумму 10 000.00 RUB успешно выполнен', 1, DATE_SUB(NOW(), INTERVAL 6 DAY), 4),
(15, 'BALANCE_CHANGE', 'Изменение баланса', 'Баланс счета 40817810099910006312 изменился на +55 000.00 RUB', 1, DATE_SUB(NOW(), INTERVAL 8 DAY), 4),
-- User 5 notifications
(16, 'TRANSACTION', 'Перевод выполнен', 'Перевод на сумму 50 000.00 RUB успешно выполнен', 1, DATE_SUB(NOW(), INTERVAL 9 DAY), 5),
(17, 'BALANCE_CHANGE', 'Изменение баланса', 'Баланс счета 40817810099910007312 изменился на +120 000.00 RUB', 1, DATE_SUB(NOW(), INTERVAL 11 DAY), 5),
(18, 'PAYMENT_DUE', 'Напоминание об оплате', 'Скоро наступит срок оплаты счета BILL0000014 (Мосэнергосбыт). Сумма: 6 500.00 RUB', 0, DATE_SUB(NOW(), INTERVAL 2 DAY), 5),
(19, 'LOAN_REMINDER', 'Напоминание о кредите', 'Не забудьте внести платеж по кредиту LOAN0000006 до 18 числа. Сумма платежа: 68 000.00 RUB', 0, DATE_SUB(NOW(), INTERVAL 4 DAY), 5);

-- Insert investments
INSERT INTO investments (id, investment_number, instrument_name, instrument_type, quantity, purchase_price, current_price, total_value, currency, created_at, user_id)
VALUES 
(1, 'INV00000001', 'Сбербанк', 'STOCK', 100.00, 250.00, 285.00, 28500.00, 'RUB', DATE_SUB(NOW(), INTERVAL 6 MONTH), 2),
(2, 'INV00000002', 'Газпром', 'STOCK', 200.00, 180.00, 195.00, 39000.00, 'RUB', DATE_SUB(NOW(), INTERVAL 8 MONTH), 2),
(3, 'INV00000003', 'Лукойл', 'STOCK', 50.00, 6500.00, 7200.00, 360000.00, 'RUB', DATE_SUB(NOW(), INTERVAL 12 MONTH), 2),
(4, 'INV00000004', 'ОФЗ 26209', 'BOND', 500000.00, 1000.00, 1025.00, 512500.00, 'RUB', DATE_SUB(NOW(), INTERVAL 18 MONTH), 2),
(5, 'INV00000005', 'Apple Inc', 'STOCK', 20.00, 150.00, 185.00, 3700.00, 'USD', DATE_SUB(NOW(), INTERVAL 10 MONTH), 2),
(6, 'INV00000006', 'Tesla Inc', 'STOCK', 5.00, 200.00, 245.00, 1225.00, 'USD', DATE_SUB(NOW(), INTERVAL 6 MONTH), 2),
(7, 'INV00000007', 'Яндекс', 'STOCK', 30.00, 2800.00, 3100.00, 93000.00, 'RUB', DATE_SUB(NOW(), INTERVAL 4 MONTH), 3),
(8, 'INV00000008', 'МТС', 'STOCK', 150.00, 320.00, 345.00, 51750.00, 'RUB', DATE_SUB(NOW(), INTERVAL 7 MONTH), 5),
(9, 'INV00000009', 'ОФЗ 26210', 'BOND', 200000.00, 1000.00, 1018.00, 203600.00, 'RUB', DATE_SUB(NOW(), INTERVAL 12 MONTH), 5),
(10, 'INV00000010', 'Microsoft Corp', 'STOCK', 10.00, 300.00, 380.00, 3800.00, 'USD', DATE_SUB(NOW(), INTERVAL 9 MONTH), 5);

-- Insert currency exchanges
INSERT INTO currency_exchanges (id, from_currency, to_currency, amount, exchange_rate, converted_amount, fee, created_at, from_account_id, to_account_id)
VALUES 
(1, 'RUB', 'USD', 70000.00, 0.011, 770.00, 500.00, DATE_SUB(NOW(), INTERVAL 30 DAY), 1, 3),
(2, 'USD', 'EUR', 500.00, 0.92, 460.00, 5.00, DATE_SUB(NOW(), INTERVAL 25 DAY), 3, 4),
(3, 'RUB', 'EUR', 50000.00, 0.0105, 525.00, 350.00, DATE_SUB(NOW(), INTERVAL 20 DAY), 1, 4),
(4, 'RUB', 'USD', 30000.00, 0.011, 330.00, 200.00, DATE_SUB(NOW(), INTERVAL 15 DAY), 1, 3),
(5, 'USD', 'RUB', 1000.00, 90.50, 90500.00, 500.00, DATE_SUB(NOW(), INTERVAL 10 DAY), 3, 1),
(6, 'RUB', 'EUR', 100000.00, 0.0105, 1050.00, 700.00, DATE_SUB(NOW(), INTERVAL 18 DAY), 10, 12);

-- Insert insurances
INSERT INTO insurances (id, policy_number, insurance_type, premium_amount, start_date, end_date, description, is_active, created_at, user_id)
VALUES 
(1, 'INS00000001', 'OSAGO', 8500.00, DATE_SUB(CURDATE(), INTERVAL 6 MONTH), DATE_ADD(CURDATE(), INTERVAL 6 MONTH), 'ОСАГО для автомобиля Toyota Camry', 1, DATE_SUB(NOW(), INTERVAL 6 MONTH), 2),
(2, 'INS00000002', 'TRAVEL', 3500.00, DATE_SUB(CURDATE(), INTERVAL 2 MONTH), DATE_ADD(CURDATE(), INTERVAL 1 MONTH), 'Страхование путешествий на отдых', 1, DATE_SUB(NOW(), INTERVAL 2 MONTH), 2),
(3, 'INS00000003', 'LIFE', 18000.00, DATE_SUB(CURDATE(), INTERVAL 12 MONTH), DATE_ADD(CURDATE(), INTERVAL 12 MONTH), 'Страхование жизни на сумму 5 000 000 RUB', 1, DATE_SUB(NOW(), INTERVAL 12 MONTH), 2),
(4, 'INS00000004', 'HEALTH', 25000.00, DATE_SUB(CURDATE(), INTERVAL 4 MONTH), DATE_ADD(CURDATE(), INTERVAL 8 MONTH), 'Добровольное медицинское страхование', 1, DATE_SUB(NOW(), INTERVAL 4 MONTH), 2),
(5, 'INS00000005', 'OSAGO', 7200.00, DATE_SUB(CURDATE(), INTERVAL 3 MONTH), DATE_ADD(CURDATE(), INTERVAL 9 MONTH), 'ОСАГО для автомобиля', 1, DATE_SUB(NOW(), INTERVAL 3 MONTH), 3),
(6, 'INS00000006', 'HEALTH', 20000.00, DATE_SUB(CURDATE(), INTERVAL 6 MONTH), DATE_ADD(CURDATE(), INTERVAL 6 MONTH), 'ДМС для семьи', 1, DATE_SUB(NOW(), INTERVAL 6 MONTH), 5),
(7, 'INS00000007', 'OSAGO', 9200.00, DATE_SUB(CURDATE(), INTERVAL 5 MONTH), DATE_ADD(CURDATE(), INTERVAL 7 MONTH), 'ОСАГО для автомобиля Mercedes', 1, DATE_SUB(NOW(), INTERVAL 5 MONTH), 5);

-- Note: All test users have password: password123
-- Default BCrypt hash: $2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iwkEx5Z2O
