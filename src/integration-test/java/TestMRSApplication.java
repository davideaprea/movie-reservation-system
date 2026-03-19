import com.mrs.app.MRSApplication;
import config.TestContainersConfiguration;
import org.springframework.boot.SpringApplication;

public class TestMRSApplication {
    public static void main(String[] args) {
        SpringApplication
                .from(MRSApplication::main)
                .with(TestContainersConfiguration.class)
                .run(args);
    }
}
