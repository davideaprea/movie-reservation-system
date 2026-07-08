package factory;

import com.mrs.app.location.dto.HallCreateRequest;
import com.mrs.app.location.entity.Cinema;
import com.mrs.app.location.entity.Hall;
import com.mrs.app.location.entity.Seat;
import com.mrs.app.location.entity.SeatType;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class HallFactory {
    public static Hall create(Cinema cinema, SeatType seatType) {
        Hall hall = new Hall(null, UUID.randomUUID().toString(), new ArrayList<>(), cinema);

        for (int i = 1; i <= 5; i++) {
            for (int j = 1; j <= 5; j++) {
                hall.addSeat(Seat.builder()
                        .hall(hall)
                        .seatNumber(j)
                        .rowNumber(i)
                        .type(seatType)
                        .build());
            }
        }

        return hall;
    }

    public static HallCreateRequest createRequest(long cinemaId, long seatTypeId, int rowsNumber, int seatsPerRow) {
        List<List<HallCreateRequest.SeatCreateRequest>> seats = new ArrayList<>();

        for (int rowNumber = 1; rowNumber <= rowsNumber; rowNumber++) {
            List<HallCreateRequest.SeatCreateRequest> row = new ArrayList<>();

            for (int seatNumber = 1; seatNumber <= seatsPerRow; seatNumber++) {
                row.add(new HallCreateRequest.SeatCreateRequest(seatTypeId));
            }

            seats.add(row);
        }

        return new HallCreateRequest(cinemaId, "Hall name", seats);
    }
}
