package module;

import annotation.ContainerizedContextTest;
import com.mrs.app.movie.dto.MovieCreateRequest;
import com.mrs.app.movie.dto.MovieResponse;
import com.mrs.app.movie.entity.Genre;
import com.mrs.app.movie.entity.Movie;
import com.mrs.app.movie.repository.GenreDAO;
import com.mrs.app.movie.repository.MovieDAO;
import dto.UserHTTPClient;
import factory.MovieFactory;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;

import java.time.Duration;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@ContainerizedContextTest
public class MovieTest {
    @Autowired
    @Qualifier("adminClient")
    private UserHTTPClient userClient;
    @Autowired
    private GenreDAO genreDAO;
    @Autowired
    private MovieDAO movieDAO;
    private Genre genre;

    @BeforeEach
    void setup() {
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
        MovieResponse response = userClient.client().post().uri("/movies")
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

        List<MovieResponse> response = userClient.client().get().uri(uriBuilder -> uriBuilder
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
