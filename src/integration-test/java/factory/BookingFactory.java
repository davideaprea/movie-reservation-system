package factory;

import com.mrs.app.booking.entity.Booking;
import com.mrs.app.booking.entity.SeatReservation;
import com.mrs.app.schedule.entity.Schedule;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BookingFactory {
    public static Booking create(Schedule schedule, List<Long> selectedSeatIds) {
        Booking bookingToSave = new Booking(null, new ArrayList<>(), schedule.getId());

        selectedSeatIds.forEach(id -> bookingToSave.addSeatReservation(new SeatReservation(null, id, bookingToSave)));

        return bookingToSave;
    }
}
