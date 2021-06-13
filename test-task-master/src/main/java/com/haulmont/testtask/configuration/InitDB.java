package com.haulmont.testtask.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

@Slf4j
@Component
public class InitDB {
    public static final String CHECK_TABLE_EXISTS_QUERY = "SELECT EXISTS (\n" +
            "   SELECT FROM information_schema.tables \n" +
            "   WHERE  table_schema = 'public'\n" +
            "   AND    table_name   = ?\n" +
            "   )";
    private final DataSource dataSource;

    public InitDB(DataSource dataSource) {
        this.dataSource = dataSource;
    }


    @PostConstruct
    public void init() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
//        Boolean exists = jdbcTemplate.query(
//                CHECK_TABLE_EXISTS_QUERY,
//                new Object[]{
//                        "clients"
//                },
//                new int[]{
//                        Types.VARCHAR
//                },
//                resultSet -> {
//                    if (resultSet.next()) {
//                        return resultSet.getBoolean(1);
//                    }
//                    return false;
//                }
//        );
//        log.info("table exists: {}", exists);
        boolean exists = false;
        if (!exists) {
            jdbcTemplate.execute("CREATE TABLE clients (\n" +
                    "   id VARCHAR(50) NOT NULL,\n" +
                    "   fool_name VARCHAR(50) NOT NULL,\n" +
                    "   telephone_number VARCHAR(20) NOT NULL,\n" +
                    "    email VARCHAR(50) NOT NULL,\n" +
                    "    passport INT NOT NULL,\n" +
                    "    bank_id VARCHAR(50) " +
                    ");");
            jdbcTemplate.execute("CREATE TABLE credits (\n" +
                    "   id VARCHAR(50) NOT NULL,\n" +
                    "   credit_limit INT NOT NULL,\n" +
                    "   interest_rate INT NOT NULL,\n" +
                    "   bank_id VARCHAR(50)" +
                    ");");
            jdbcTemplate.execute("CREATE TABLE offers (\n" +
                    "   id VARCHAR(50) NOT NULL,\n" +
                    "   client_id VARCHAR(50) NOT NULL,\n" +
                    "   credit_id VARCHAR(50) NOT NULL,\n" +
                    "   credit_amount INT NOT NULL " +
                    ");");
            jdbcTemplate.execute("CREATE TABLE banks (\n" +
                    "  id VARCHAR(50) NOT NULL,\n" +
                    "  name VARCHAR(50) NOT NULL" +
                    ");");


//            jdbcTemplate.execute("CREATE TABLE public.items\n" +
//                    "(\n" +
//                    "    id bigint NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 9223372036854775807 CACHE 1 ),\n" +
//                    "    name character varying COLLATE pg_catalog.\"default\" NOT NULL,\n" +
//                    "    description character varying COLLATE pg_catalog.\"default\" NOT NULL,\n" +
//                    "    category_id bigint NOT NULL,\n" +
//                    "    price numeric(20,2),\n" +
//                    "    CONSTRAINT \"Items_pkey\" PRIMARY KEY (id)\n" +
//                    ")");
        }
    }
}
