package com.haulmont.testtask.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.sql.Types;

@Slf4j
@Component
public class InitDB {
    public static final String CHECK_TABLE_EXISTS_QUERY = "select schema_name \n" +
            "   FROM information_schema.schemata \n" +
            "   WHERE  schema_name = ?";
    private final DataSource dataSource;

    public InitDB(DataSource dataSource) {
        this.dataSource = dataSource;
    }


    @PostConstruct
    public void init() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        Boolean existsClients = jdbcTemplate.query(
                CHECK_TABLE_EXISTS_QUERY,
                new Object[]{
                        "clients"
                },
                new int[]{
                        Types.VARCHAR
                },
                resultSet -> {
                    if (resultSet.next()) {
                        return resultSet.getBoolean(1);
                    }
                    return false;
                }
        );
        log.info("table exists: {}", existsClients);
        if (!existsClients) {
            jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS clients (\n" +
                    "   id VARCHAR(50) NOT NULL, \n" +
                    "   fool_name VARCHAR(50) NOT NULL,\n" +
                    "   telephone_number VARCHAR(50) NOT NULL,\n" +
                    "    email VARCHAR(50) NOT NULL,\n" +
                    "    passport INT NOT NULL UNIQUE,\n" +
                    "    bank_id VARCHAR(50), \n" +
                    "    PRIMARY KEY (id) " +
                    ");");
            jdbcTemplate.execute("CREATE TABLE credits (\n" +
                    "   id VARCHAR(50) NOT NULL,\n" +
                    "   credit_limit INT NOT NULL,\n" +
                    "   interest_rate INT NOT NULL,\n" +
                    "   bank_id VARCHAR(50),\n" +
                    "    PRIMARY KEY (id) " +
                    ");");
            jdbcTemplate.execute("CREATE TABLE offers (\n" +
                    "   id VARCHAR(50) NOT NULL,\n" +
                    "   client_id VARCHAR(50) NOT NULL,\n" +
                    "   credit_id VARCHAR(50) NOT NULL,\n" +
                    "   credit_amount INT NOT NULL,\n " +
                    "   date VARCHAR(50) NOT NULL, \n " +
                    "    PRIMARY KEY (id) " +
                    ");");
            jdbcTemplate.execute("CREATE TABLE banks (\n" +
                    "  id VARCHAR(50) NOT NULL,\n" +
                    "  name VARCHAR(50) NOT NULL," +
                    "    PRIMARY KEY (id) " +
                    ");");


//            jdbcTemplate.execute("CREATE TABLE public.clients\n" +
//                    "(\n" +
//                    "   id VARCHAR(50) NOT NULL, \n" +
//                    "   fool_name VARCHAR(50) NOT NULL,\n" +
//                    "   telephone_number VARCHAR(20) NOT NULL,\n" +
//                    "    email VARCHAR(50) NOT NULL,\n" +
//                    "    passport INT NOT NULL UNIQUE,\n" +
//                    "    bank_id VARCHAR(50), \n" +
//                    "    PRIMARY KEY (id) " +
//                    ")");
        }
    }
}
