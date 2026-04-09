package factory;

import com.mrs.app.movie.entity.Movie;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MovieFactory {
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
