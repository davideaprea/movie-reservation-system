package config;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.AbstractTestExecutionListener;

import java.util.List;

public class DataBaseCleanerConfig extends AbstractTestExecutionListener {
    @Override
    public void beforeTestMethod(TestContext testContext) {
        JdbcTemplate jdbcTemplate = testContext.getApplicationContext().getBean(JdbcTemplate.class);

        List<String> tableNames = jdbcTemplate.queryForList(
                "SELECT tablename FROM pg_tables WHERE schemaname = 'public' AND tablename != 'users'",
                String.class
        );

        if (!tableNames.isEmpty()) {
            String tables = String.join(", ", tableNames);
            jdbcTemplate.execute("TRUNCATE TABLE " + tables + " RESTART IDENTITY CASCADE");
        }
    }
}
