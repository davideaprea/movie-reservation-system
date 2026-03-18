package schedule;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mrs.app.schedule.dto.ScheduleCreateRequest;
import com.mrs.app.schedule.dto.ScheduleResponse;
import dto.HttpResponse;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@Component
@Profile("test")
@AllArgsConstructor
public class ScheduleClient {
    private final MockMvc mockClient;
    private final ObjectMapper objectMapper;

    @SneakyThrows
    public HttpResponse<ScheduleResponse> create(ScheduleCreateRequest payload) {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/schedules")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(payload));
        MockHttpServletResponse response = mockClient
                .perform(requestBuilder)
                .andReturn()
                .getResponse();
        ScheduleResponse body = objectMapper.readValue(response.getContentAsString(), ScheduleResponse.class);

        return new HttpResponse<>(body, HttpStatus.valueOf(response.getStatus()));
    }
}
