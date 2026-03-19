package factory;

import com.mrs.app.movie.entity.Movie;

import java.time.Duration;

public class MovieFactory {
    private MovieFactory() {
    }

    public static Movie create() {
        return Movie.builder()
                .cover("cover-link")
                .description("Description")
                .title("Title")
                .duration(Duration.ofHours(2))
                .build();
    }
}
