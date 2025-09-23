package Core.Domain;

import java.time.LocalDate;

public class ClosedDay {
    private final LocalDate date;
    private final String reason;

    public ClosedDay(LocalDate date, String reason) {
        this.date = date;
        this.reason = reason;
    }

    public LocalDate getDate() { return date; }
    public String getReason() { return reason; }
}
