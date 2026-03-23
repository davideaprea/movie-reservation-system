package factory;

import com.mrs.app.booking.entity.Booking;
import com.mrs.app.booking.entity.SeatReservation;
import com.mrs.app.schedule.entity.Schedule;

import java.util.ArrayList;

public class BookingFactory {
    private BookingFactory() {
    }

    public static Booking create(Schedule schedule) {
        Booking bookingToSave = new Booking(null, new ArrayList<>(), schedule.getId());

        bookingToSave.addSeatReservation(new SeatReservation(null, schedule.getSeats().getFirst().getId(), bookingToSave));

        return bookingToSave;
    }
}
