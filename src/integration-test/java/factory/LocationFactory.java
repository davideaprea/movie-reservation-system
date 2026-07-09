package factory;

import com.mrs.app.location.entity.Address;
import com.mrs.app.location.entity.Cinema;
import com.mrs.app.location.entity.City;
import com.mrs.app.location.entity.Region;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LocationFactory {
    public static Region createRegion() {
        return new Region(null, UUID.randomUUID().toString(), UUID.randomUUID().toString());
    }

    public static City createCity(Region region) {
        return new City(null, UUID.randomUUID().toString(), UUID.randomUUID().toString(), region);
    }

    public static Address createAddress(City city) {
        return new Address(null, city, UUID.randomUUID().toString(), UUID.randomUUID().toString());
    }

    public static Cinema createCinema(Address address) {
        return new Cinema(null, address, UUID.randomUUID().toString());
    }
}
