package factory;

import com.mrs.booking.entity.Booking;
import com.mrs.booking.entity.SeatReservation;
import com.mrs.schedule.entity.Schedule;
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
