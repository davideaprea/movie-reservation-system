package module;

import annotation.ContainerizedContextTest;
import com.mrs.location.dto.HallCreateRequest;
import com.mrs.location.dto.HallResponse;
import com.mrs.location.entity.*;
import com.mrs.location.repository.*;
import com.mrs.security.component.JWTCreator;
import com.mrs.security.repository.UserRepository;
import com.mrs.security.dto.JWTClaims;
import com.mrs.security.entity.User;
import factory.LocationFactory;
import factory.HallFactory;
import factory.UserFactory;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.web.servlet.client.RestTestClient;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@ContainerizedContextTest
public class LocationTest {
    private RestTestClient restTestClient;
    @Autowired
    private HallRepository hallRepository;
    @Autowired
    private SeatRepository seatRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JWTCreator jwtCreator;
    @LocalServerPort
    private int port;
    @Autowired
    private SeatTypeRepository seatTypeRepository;
    private SeatType standardSeatType;
    @Autowired
    private CinemaRepository cinemaRepository;
    private Cinema cinema;
    @Autowired
    private RegionRepository regionRepository;
    @Autowired
    private CityRepository cityRepository;
    @Autowired
    private AddressRepository addressRepository;

    @BeforeEach
    void setup() {
        Region region = regionRepository.save(LocationFactory.createRegion());
        City city = cityRepository.save(LocationFactory.createCity(region));
        Address address = addressRepository.save(LocationFactory.createAddress(city));
        cinema = cinemaRepository.save(LocationFactory.createCinema(address));
        User user = userRepository.save(UserFactory.createOperator(cinema.getId()));
        String jwt = jwtCreator.withSubject(new JWTClaims(user.getEmail(), List.of(user.getRole().toString())));
        restTestClient = RestTestClient
                .bindToServer()
                .baseUrl("http://localhost:%d".formatted(port))
                .defaultHeader("Authorization", "Bearer " + jwt)
                .build();
        standardSeatType = seatTypeRepository.save(new SeatType(null, "STANDARD"));
    }

    @SneakyThrows
    @Test
    void givenValidPayload_whenCreatingHall_thenStatusCreated() {
        int rowsNumber = 5;
        int seatsPerRow = 5;
        HallCreateRequest request = HallFactory.createRequest(cinema.getId(), standardSeatType.getId(), rowsNumber, seatsPerRow);

        restTestClient.post().uri("/halls")
                .body(request).exchange()
                .expectStatus().isCreated()
                .expectBody(HallResponse.class);

        assertThat(hallRepository.count()).isEqualTo(1);
        assertThat(seatRepository.count()).isEqualTo(rowsNumber * seatsPerRow);
    }
}
