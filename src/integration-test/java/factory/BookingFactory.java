package factory;

import com.mrs.app.booking.entity.Booking;
import com.mrs.app.booking.entity.SeatReservation;
import com.mrs.app.schedule.entity.Schedule;

import java.util.ArrayList;
import java.util.List;

public class BookingFactory {
    private BookingFactory() {
    }

    public static Booking create(Schedule schedule, List<Long> selectedSeatIds) {
        Booking bookingToSave = new Booking(null, new ArrayList<>(), schedule.getId());

        selectedSeatIds.forEach(id -> bookingToSave.addSeatReservation(new SeatReservation(null, id, bookingToSave)));

        return bookingToSave;
    }
}
