package module;

import annotation.ContainerizedContextTest;
import io.github.davideaprea.movie.dto.MovieCreateRequest;
import io.github.davideaprea.movie.dto.MovieResponse;
import io.github.davideaprea.movie.entity.Genre;
import io.github.davideaprea.movie.entity.Movie;
import io.github.davideaprea.movie.repository.GenreDAO;
import io.github.davideaprea.movie.repository.MovieDAO;
import io.github.davideaprea.security.component.JWTCreator;
import io.github.davideaprea.security.dao.UserDAO;
import io.github.davideaprea.security.dto.JWTClaims;
import io.github.davideaprea.security.entity.User;
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
    private UserDAO userDAO;
    @Autowired
    private JWTCreator jwtCreator;
    @LocalServerPort
    private int port;
    @Autowired
    private GenreDAO genreDAO;
    @Autowired
    private MovieDAO movieDAO;

    private Genre genre;

    @BeforeEach
    void setup() {
        User user = userDAO.save(UserFactory.createAdmin());
        String jwt = jwtCreator.withSubject(new JWTClaims(user.getEmail(), List.of(user.getRole().getValue())));
        restTestClient = RestTestClient
                .bindToServer()
                .baseUrl("http://localhost:%d".formatted(port))
                .defaultHeader("Authorization", "Bearer " + jwt)
                .build();
        genre = genreDAO.save(new Genre(null, "ACTION"));
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
        Movie createdMovie = movieDAO.findById(response.id()).get();

        assertThat(movieDAO.count()).isEqualTo(1);
        assertThat(request.duration()).isEqualTo(createdMovie.getDuration());
        assertThat(request.genreIds()).containsExactlyInAnyOrderElementsOf(createdMovie.getGenres().stream().map(Genre::getId).toList());
    }

    @SneakyThrows
    @Test
    void givenValidParam_whenFindingAllMovies_thenStatusOk() {
        String title = "Title";
        String randomString = "lorem";

        movieDAO.saveAll(List.of(
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
