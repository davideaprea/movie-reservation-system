package factory;

import io.github.davideaprea.booking.entity.Booking;
import io.github.davideaprea.booking.entity.SeatReservation;
import io.github.davideaprea.schedule.entity.Schedule;

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
