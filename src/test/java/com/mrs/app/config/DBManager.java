package com.mrs.app.config;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.AbstractTestExecutionListener;

import java.util.List;

public class DBManager extends AbstractTestExecutionListener {
    @Override
    public void beforeTestMethod(TestContext testContext) {
        JdbcTemplate jdbcTemplate = testContext.getApplicationContext().getBean(JdbcTemplate.class);

        jdbcTemplate.execute("SET session_replication_role = 'replica';");

        List<String> tableNames = jdbcTemplate.queryForList("SELECT tablename FROM pg_tables WHERE schemaname = 'public'", String.class);

        for (String table : tableNames) {
            jdbcTemplate.execute("TRUNCATE TABLE " + table + " RESTART IDENTITY CASCADE");
        }

        jdbcTemplate.execute("SET session_replication_role = 'origin';");
    }
}
