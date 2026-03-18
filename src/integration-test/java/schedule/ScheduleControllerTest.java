package schedule;

import com.mrs.app.schedule.dto.ScheduleCreateRequest;
import com.mrs.app.schedule.dto.ScheduleResponse;
import dto.HttpResponse;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

public class ScheduleControllerTest {
    private ScheduleClient scheduleClient;

    @SneakyThrows
    @Test
    void test() {
        ScheduleCreateRequest request = new ScheduleCreateRequest(
                1,
                1,
                LocalDateTime.now().plusDays(1),
                Map.of("STANDARD", BigDecimal.valueOf(5))
        );
        HttpResponse<ScheduleResponse> response = scheduleClient.create(request, ScheduleResponse.class);

        Assertions.assertEquals(HttpStatus.CREATED, response.status());
        Assertions.assertNotNull(response.body());
    }
}
