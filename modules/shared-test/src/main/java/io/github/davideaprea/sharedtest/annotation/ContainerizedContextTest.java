package io.github.davideaprea.sharedtest.annotation;

import io.github.davideaprea.sharedtest.config.BasePackagesScanner;
import io.github.davideaprea.sharedtest.config.DataBaseCleaner;
import io.github.davideaprea.sharedtest.config.TestContainersConfiguration;
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
        classes = BasePackagesScanner.class
)
@Import({TestContainersConfiguration.class})
public @interface ContainerizedContextTest {
}
