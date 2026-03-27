package annotation;

import com.mrs.app.MRSApplication;
import config.DataBaseCleanerConfig;
import config.UserHTTPClientConfig;
import config.PostgreSQLContainerConfig;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestExecutionListeners;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@TestExecutionListeners(
        value = DataBaseCleanerConfig.class,
        mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS
)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = MRSApplication.class
)
@Import({PostgreSQLContainerConfig.class, UserHTTPClientConfig.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public @interface ContainerizedTest {
}
