package factory;

import com.mrs.app.movie.entity.Movie;

import java.time.Duration;
import java.util.UUID;

public class MovieFactory {

    private MovieFactory() {
    }

    public static Movie create() {
        return Movie.builder()
                .coverImageLink("cover-link")
                .description("Description")
                .title("Random string as title " + UUID.randomUUID())
                .duration(Duration.ofHours(2))
                .build();
    }

    public static Movie create(String title) {
        return Movie.builder()
                .coverImageLink("cover-link")
                .description("Description")
                .title(title)
                .duration(Duration.ofHours(2))
                .build();
    }
}
