package factory;

import com.mrs.app.hall.dto.HallCreateRequest;
import com.mrs.app.hall.entity.Hall;
import com.mrs.app.hall.entity.Seat;
import com.mrs.app.hall.entity.SeatType;

import java.util.ArrayList;
import java.util.List;

public class HallFactory {
    private HallFactory() {
    }

    public static Hall create(SeatType seatType) {
        Hall hall = new Hall(null, "Name", new ArrayList<>());

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

    public static HallCreateRequest createRequest(long seatTypeId, int rowsNumber, int seatsPerRow) {
        List<List<HallCreateRequest.SeatCreateRequest>> seats = new ArrayList<>();

        for (int rowNumber = 1; rowNumber <= rowsNumber; rowNumber++) {
            List<HallCreateRequest.SeatCreateRequest> row = new ArrayList<>();

            for (int seatNumber = 1; seatNumber <= seatsPerRow; seatNumber++) {
                row.add(new HallCreateRequest.SeatCreateRequest(seatTypeId));
            }

            seats.add(row);
        }

        return new HallCreateRequest("Hall name", seats);
    }
}
