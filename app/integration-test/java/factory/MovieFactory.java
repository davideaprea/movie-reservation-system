package factory;

import com.github.javafaker.Faker;
import io.github.davideaprea.movie.entity.Movie;

import java.time.Duration;

public class MovieFactory {
    private static final Faker faker = new Faker();

    private MovieFactory() {
    }

    public static Movie create() {
        return Movie.builder()
                .coverImageLink("cover-link")
                .description("Description")
                .title(faker.book().title())
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
