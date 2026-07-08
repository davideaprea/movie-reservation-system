package factory;

import com.mrs.app.location.entity.Cinema;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CinemaFactory {
    public static Cinema create() {
        return new Cinema(null, new Cinema.Address(
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString()
        ), UUID.randomUUID().toString());
    }
}
