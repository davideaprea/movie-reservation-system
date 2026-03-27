package annotation;

import com.mrs.app.MRSApplication;
import config.DataBaseCleaner;
import config.RestTestClientConfig;
import config.TestContainersConfiguration;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestExecutionListeners;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@TestExecutionListeners(
        value = DataBaseCleaner.class,
        mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS
)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = MRSApplication.class
)
@Import({TestContainersConfiguration.class, RestTestClientConfig.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public @interface ContainerizedContextTest {
}
