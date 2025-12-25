package com.bank.Bank.config;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class DatabaseInitializer {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseInitializer.class);

    @PostConstruct
    public void initialize() {
        logger.info("DatabaseInitializer: Инициализация базы данных. Hibernate должен создать таблицы автоматически с настройкой ddl-auto=create");
        logger.info("Если таблицы не создаются, проверьте:");
        logger.info("1. Подключение к базе данных bank_app");
        logger.info("2. Права пользователя на создание таблиц");
        logger.info("3. Логи Hibernate на наличие SQL CREATE TABLE");
    }
}

