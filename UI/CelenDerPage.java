import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.time.*;

public class CelenDerPage extends JFrame {

    public CelenDerPage() {
        setTitle("Booking UI with Calendar");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(512, 576);          // ขนาดเริ่มต้น
        setLocationRelativeTo(null);
        setResizable(true);         // ✅ สามารถกดเต็มจอได้

        setLayout(new BorderLayout());

        // ===== Top Bar =====
        JPanel topBar = new JPanel();
        topBar.setBackground(new Color(235,140,40));
        topBar.setLayout(new BoxLayout(topBar, BoxLayout.X_AXIS));
        topBar.setBorder(new EmptyBorder(6,10,6,10));

        JButton bookingBtn = pillButton("Booking", true);
        JButton profileBtn = pillButton("Profile", false);
        JLabel hiUser = new JLabel("Hi! User");
        hiUser.setForeground(new Color(60,40,20));
        JButton leaveBtn = pillButton("Leave", false);
        leaveBtn.addActionListener(e -> dispose());
        profileBtn.addActionListener(e -> { new UserSettingForm().setVisible(true); dispose(); }); 

        topBar.add(bookingBtn);
        topBar.add(Box.createHorizontalStrut(10));
        topBar.add(profileBtn);
        topBar.add(Box.createHorizontalGlue());
        topBar.add(hiUser);
        topBar.add(Box.createHorizontalGlue());
        topBar.add(leaveBtn);

        add(topBar, BorderLayout.NORTH);

        // ===== Content Area (gray background with orange frame) =====
        JPanel contentArea = new JPanel(new BorderLayout());
        contentArea.setBackground(new Color(54,54,54));
        contentArea.setBorder(new CompoundBorder(
                new EmptyBorder(10,10,10,10),
                new LineBorder(new Color(235,140,40), 8, true)
        ));

        // ใส่ CalendarPanel ตรงกลาง
        contentArea.add(new CalendarPanel(), BorderLayout.CENTER);

        add(contentArea, BorderLayout.CENTER);
    }

    private JButton pillButton(String text, boolean light) {
        JButton b = new JButton(text);
        b.setFocusPainted(false);
        b.setBorder(new EmptyBorder(6,18,6,18));
        b.setBackground(light ? Color.WHITE : new Color(120,120,120));
        b.setForeground(light ? new Color(50,50,50) : Color.WHITE);
        return b;
    }

    // ===== Calendar Panel (จากโค้ดปฏิทินก่อนหน้า) =====
    static class CalendarPanel extends JPanel {
        private LocalDate shownMonth = LocalDate.now().withDayOfMonth(1);
        private final JLabel monthLabel = new JLabel("", SwingConstants.CENTER);
        private final JPanel grid = new JPanel(new GridLayout(0,7,6,6));

        CalendarPanel() {
            setOpaque(false);
            setLayout(new BorderLayout(8,8));

            // header
            JPanel header = new JPanel(new BorderLayout());
            header.setOpaque(false);
            JButton prev = nav("‹");
            JButton next = nav("›");
            prev.addActionListener(e -> { shownMonth = shownMonth.minusMonths(1); rebuild(); });
            next.addActionListener(e -> { shownMonth = shownMonth.plusMonths(1); rebuild(); });

            monthLabel.setForeground(Color.WHITE);
            monthLabel.setFont(monthLabel.getFont().deriveFont(Font.BOLD, 16f));

            header.add(prev, BorderLayout.WEST);
            header.add(monthLabel, BorderLayout.CENTER);
            header.add(next, BorderLayout.EAST);

            // weekday header
            JPanel week = new JPanel(new GridLayout(1,7,6,6));
            week.setOpaque(false);
            String[] wd = {"Mon","Tue","Wed","Thu","Fri","Sat","Sun"};
            for (String s : wd) {
                JLabel l = new JLabel(s, SwingConstants.CENTER);
                l.setForeground(Color.WHITE);
                week.add(l);
            }

            // frame
            JPanel frame = new JPanel(new BorderLayout(6,6));
            frame.setOpaque(false);
            frame.setBorder(new CompoundBorder(
                    new LineBorder(new Color(235,140,40), 6, true),
                    new EmptyBorder(8,8,8,8)
            ));
            grid.setOpaque(false);
            frame.add(week, BorderLayout.NORTH);
            frame.add(grid, BorderLayout.CENTER);

            add(header, BorderLayout.NORTH);
            add(frame, BorderLayout.CENTER);

            rebuild();
        }

        private JButton nav(String t) {
            JButton b = new JButton(t);
            b.setFocusPainted(false);
            b.setBackground(new Color(120,120,120));
            b.setForeground(Color.WHITE);
            return b;
        }

        private void rebuild() {
            grid.removeAll();

            YearMonth ym = YearMonth.from(shownMonth);
            LocalDate first = ym.atDay(1);
            LocalDate today = LocalDate.now();
            int days = ym.lengthOfMonth();

            monthLabel.setText(ym.getMonth().toString() + " " + ym.getYear());

            int lead = first.getDayOfWeek().getValue() - 1;
            for (int i=0;i<lead;i++) grid.add(new JLabel(""));

            for (int d=1; d<=days; d++) {
                LocalDate date = ym.atDay(d);
                JButton btn = new JButton(String.valueOf(d));
                styleDay(btn);
                boolean enabled = !date.isBefore(today);
                btn.setEnabled(enabled);

                if (enabled) {
    final String selectedDate = "seelect "+date; // ถ้าจะส่งต่อวันที่
    btn.addActionListener(e -> {
        // เปิดหน้า Booking
        new BookingPage().setVisible(true);
        // ปิดหน้าต่างปัจจุบัน (ปิด Window ancestor)
        java.awt.Window w = SwingUtilities.getWindowAncestor(CalendarPanel.this);
        if (w != null) w.dispose();
    });
}
grid.add(btn);
                
            }

            int cells = lead + days;
            int tail = (7 - (cells % 7)) % 7;
            for (int i=0;i<tail;i++) grid.add(new JLabel(""));

            revalidate();
            repaint();
        }

        private void styleDay(AbstractButton b) {
            b.setFocusPainted(false);
            b.setBackground(new Color(90,90,90));
            b.setForeground(Color.WHITE);
            b.setBorder(new CompoundBorder(
                    new LineBorder(new Color(235,140,40), 3, true),
                    new EmptyBorder(8,0,8,0)
            ));
        }
    }
    public static void main(String[] args) {
        UIManager.put("Panel.background", new Color(54,54,54));
        SwingUtilities.invokeLater(() -> new CelenDerPage().setVisible(true));
    }
    }

