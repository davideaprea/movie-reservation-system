package io.github.davideaprea.sharedtest.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {
        "io.github.davideaprea"
})
public class BasePackagesScanner {
}
