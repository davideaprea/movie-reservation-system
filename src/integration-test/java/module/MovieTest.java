package module;

import annotation.ContainerizedContextTest;
import com.mrs.app.movie.dto.MovieCreateRequest;
import com.mrs.app.movie.dto.MovieResponse;
import com.mrs.app.movie.entity.Genre;
import com.mrs.app.movie.entity.Movie;
import com.mrs.app.movie.repository.GenreRepository;
import com.mrs.app.movie.repository.MovieRepository;
import com.mrs.app.security.component.JWTCreator;
import com.mrs.app.security.repository.UserRepository;
import com.mrs.app.security.dto.JWTClaims;
import com.mrs.app.security.entity.User;
import factory.MovieFactory;
import factory.UserFactory;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.test.web.servlet.client.RestTestClient;

import java.time.Duration;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@ContainerizedContextTest
public class MovieTest {
    private RestTestClient restTestClient;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JWTCreator jwtCreator;
    @LocalServerPort
    private int port;
    @Autowired
    private GenreRepository genreRepository;
    @Autowired
    private MovieRepository movieRepository;

    private Genre genre;

    @BeforeEach
    void setup() {
        User user = userRepository.save(UserFactory.createAdmin());
        String jwt = jwtCreator.withSubject(new JWTClaims(user.getEmail(), List.of(user.getRole().getValue())));
        restTestClient = RestTestClient
                .bindToServer()
                .baseUrl("http://localhost:%d".formatted(port))
                .defaultHeader("Authorization", "Bearer " + jwt)
                .build();
        genre = genreRepository.save(new Genre(null, "ACTION"));
    }

    @SneakyThrows
    @Test
    void givenValidPayload_whenCreatingAMovie_thenStatusCreated() {
        MovieCreateRequest request = new MovieCreateRequest(
                "Title",
                Duration.ofHours(2),
                "Detailed movie description",
                "cover-link",
                List.of(genre.getId())
        );
        MovieResponse response = restTestClient.post().uri("/movies")
                .body(request).exchange()
                .expectStatus().isCreated()
                .expectBody(MovieResponse.class)
                .returnResult().getResponseBody();
        Movie createdMovie = movieRepository.findById(response.id()).get();

        assertThat(movieRepository.count()).isEqualTo(1);
        assertThat(request.duration()).isEqualTo(createdMovie.getDuration());
        assertThat(request.genreIds()).containsExactlyInAnyOrderElementsOf(createdMovie.getGenres().stream().map(Genre::getId).toList());
    }

    @SneakyThrows
    @Test
    void givenValidParam_whenFindingAllMovies_thenStatusOk() {
        String title = "Title";
        String randomString = "lorem";

        movieRepository.saveAll(List.of(
                MovieFactory.create(title + " " + randomString),
                MovieFactory.create(randomString + " " + title),
                MovieFactory.create(randomString + " " + title + " " + randomString),
                MovieFactory.create("Different")
        ));

        List<MovieResponse> response = restTestClient.get().uri(uriBuilder -> uriBuilder
                        .path("/movies")
                        .queryParam("title", title)
                        .build())
                .exchange().expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<List<MovieResponse>>() {
                }).returnResult().getResponseBody();

        assertThat(response.size()).isEqualTo(3);
        assertThat(response)
                .extracting(MovieResponse::title)
                .allMatch(t -> t.contains(title));
    }
}
