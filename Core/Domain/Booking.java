package Core.Domain;

import java.time.LocalDate;

public class Booking {
    private final String id;
    private final String userEmail;
    private final LocalDate date;
    private final TimeSlot slot;
    private BookingStatus status;

    public Booking(String id, String userEmail, LocalDate date, TimeSlot slot, BookingStatus status) {
        this.id = id;
        this.userEmail = userEmail;
        this.date = date;
        this.slot = slot;
        this.status = status;
    }

    public String getId() { return id; }
    public String getUserEmail() { return userEmail; }
    public LocalDate getDate() { return date; }
    public TimeSlot getSlot() { return slot; }
    public BookingStatus getStatus() { return status; }

    public void setStatus(BookingStatus status) { this.status = status; }
}
