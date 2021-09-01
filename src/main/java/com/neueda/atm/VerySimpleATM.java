package com.neueda.atm;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import javax.sql.DataSource;

@SpringBootApplication
@EnableJpaRepositories
public class VerySimpleATM {
    private static final Logger logger = LogManager.getLogger(VerySimpleATM.class);



    public static void main(String[] args) {
        SpringApplication.run(VerySimpleATM.class, args);
    }


}
