-- SQL script to drop all tables if they exist
-- Use this script manually in MySQL to clean up the database

SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS currency_exchanges;
DROP TABLE IF EXISTS investments;
DROP TABLE IF EXISTS insurances;
DROP TABLE IF EXISTS expenses;
DROP TABLE IF EXISTS budgets;
DROP TABLE IF EXISTS payment_templates;
DROP TABLE IF EXISTS bills;
DROP TABLE IF EXISTS transfers;
DROP TABLE IF EXISTS deposits;
DROP TABLE IF EXISTS loans;
DROP TABLE IF EXISTS cards;
DROP TABLE IF EXISTS transactions;
DROP TABLE IF EXISTS accounts;
DROP TABLE IF EXISTS notifications;
DROP TABLE IF EXISTS users;

SET FOREIGN_KEY_CHECKS = 1;


