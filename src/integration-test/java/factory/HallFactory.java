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

    public static HallCreateRequest createRequest(long seatTypeId) {
        List<HallCreateRequest.SeatDTO> seats = new ArrayList<>();

        for (int rowNumber = 1; rowNumber <= 5; rowNumber++) {
            for (int seatNumber = 1; seatNumber <= 5; seatNumber++) {
                seats.add(new HallCreateRequest.SeatDTO(rowNumber, seatNumber, seatTypeId));
            }
        }

        return new HallCreateRequest("Hall name", seats);
    }
}
