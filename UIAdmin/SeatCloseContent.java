package UIAdmin;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.LinkedHashMap;
import java.util.Map;

public class SeatCloseContent extends JPanel {

    private final JSpinner dateSpinner;
    private final JButton closeTodayBtn;
    private final Map<String, JTextField> capacityFields = new LinkedHashMap<>();

    private static final String[] SLOTS = {
            "09:00–10:00", "10:00–11:00", "11:00–12:00",
            "12:00–13:00", "14:00–15:00", "15:00–16:00"
    };

    public SeatCloseContent() {
        setPreferredSize(new Dimension(512, 506));
        setMinimumSize(new Dimension(512, 506));
        setMaximumSize(new Dimension(512, 506));
        setLayout(new BorderLayout(0, 16));
        setBorder(new EmptyBorder(12, 12, 12, 12));

        // ---- Controls Row (Label -> Spinner -> Button) ----
JPanel controls = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
controls.setOpaque(false);

JLabel dateLbl = new JLabel("Date:");

dateSpinner = new JSpinner(new SpinnerDateModel());
// 👇 ใช้รูปแบบ วัน/เดือน/ปี
JSpinner.DateEditor editor = new JSpinner.DateEditor(dateSpinner, "dd/MM/yyyy");
dateSpinner.setEditor(editor);
dateSpinner.setPreferredSize(new Dimension(120, 28));

closeTodayBtn = new JButton("Close Today");

// Add in order: Label -> Spinner -> Button
controls.add(dateLbl);
controls.add(dateSpinner);
controls.add(closeTodayBtn);



        
        

        // ---- Grid of time-slot cards ----
        JPanel grid = new JPanel(new GridLayout(3, 2, 12, 12));
        grid.setOpaque(false);

        for (String slot : SLOTS) {
            grid.add(makeSlotCard(slot));
        }

        // ---- Compose ----
        add(controls, BorderLayout.NORTH);
        add(grid, BorderLayout.CENTER);
    }

    private JPanel makeSlotCard(String slotText) {
        JPanel card = new JPanel();
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(220, 220, 220)),
                new EmptyBorder(8, 10, 8, 10)
        ));
        card.setLayout(new BorderLayout(0, 6));

        JLabel slot = new JLabel(slotText);
        slot.setFont(slot.getFont().deriveFont(Font.BOLD, 14f));

        JLabel capLbl = new JLabel("Remaining capacity");
        capLbl.setForeground(Color.DARK_GRAY);

        JTextField capField = new JTextField("8");
        capField.setPreferredSize(new Dimension(100, 28));
        capacityFields.put(slotText, capField);

        JPanel body = new JPanel(new BorderLayout(0, 4));
        body.setOpaque(false);
        body.add(capLbl, BorderLayout.NORTH);
        body.add(capField, BorderLayout.CENTER);

        card.add(slot, BorderLayout.NORTH);
        card.add(body, BorderLayout.CENTER);

        return card;
    }

    // Example test run
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame f = new JFrame("Test Content");
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            f.getContentPane().add(new SeatCloseContent());
            f.pack();
            f.setLocationRelativeTo(null);
            f.setVisible(true);
        });
    }
}
