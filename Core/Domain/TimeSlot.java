package Core.Domain;

import java.time.LocalTime;

public class TimeSlot {
    private final String label;
    private final LocalTime start;
    private final LocalTime end;

    public TimeSlot(String label, LocalTime start, LocalTime end) {
        this.label = label;
        this.start = start;
        this.end = end;
    }

    public String getLabel() { return label; }
    public LocalTime getStart() { return start; }
    public LocalTime getEnd() { return end; }

    @Override
    public String toString() { return label; }

    public static TimeSlot[] defaultSlots() {
        return new TimeSlot[] {
            new TimeSlot("09:00–10:00", LocalTime.of(9,0), LocalTime.of(10,0)),
            new TimeSlot("10:00–11:00", LocalTime.of(10,0), LocalTime.of(11,0)),
            new TimeSlot("11:00–12:00", LocalTime.of(11,0), LocalTime.of(12,0)),
            new TimeSlot("13:00–14:00", LocalTime.of(13,0), LocalTime.of(14,0)),
            new TimeSlot("14:00–15:00", LocalTime.of(14,0), LocalTime.of(15,0)),
            new TimeSlot("15:00–16:00", LocalTime.of(15,0), LocalTime.of(16,0)),
        };
    }
}
