package Core.Domain;

import java.time.LocalDate;

public class ClosedSlot {
    private final LocalDate date;
    private final String slotLabel;
    private final String reason;

    public ClosedSlot(LocalDate date, String slotLabel, String reason) {
        this.date = date;
        this.slotLabel = slotLabel;
        this.reason = reason;
    }

    public LocalDate getDate() { return date; }
    public String getSlotLabel() { return slotLabel; }
    public String getReason() { return reason; }
}
