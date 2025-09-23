package UIAdmin;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;

public class AdminBooking extends JPanel {

    private JTable table;

    public AdminBooking() {
        setOpaque(false);
        setLayout(new GridBagLayout()); // เพื่อให้กรอบส้มเต็มพื้นที่

        // ===== Outer orange rectangular panel (ขอบตรง) =====
        JPanel outer = new JPanel(new BorderLayout());
        outer.setBackground(new Color(243,144,47)); // ส้ม
        outer.setBorder(new EmptyBorder(6,6,6,6));  // เว้นขอบให้เห็นกรอบส้ม
        add(outer, gbc());

        // ===== Inner dark panel =====
        JPanel inner = new JPanel(new BorderLayout());
        inner.setBackground(new Color(48,48,48)); // เทาเข้ม
        inner.setBorder(new EmptyBorder(8,8,8,8));
        outer.add(inner, BorderLayout.CENTER);

        // ===== Table (WITHOUT "Code" column) =====
        String[] cols = {"Date", "Time", "Number", "Customer", "Total"};
        Object[][] rows = {
            {"2025-09-22", "15:00-16:00", 1, "pacharaxxxx@gmail.com", "$150.00"}
        };
        table = new JTable(new DefaultTableModel(rows, cols) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        });

        styleTable(table);

        JScrollPane sp = new JScrollPane(table);
        sp.getViewport().setBackground(new Color(54,54,54));
        sp.setBorder(BorderFactory.createMatteBorder(1,0,0,0,new Color(90,90,90)));

        inner.add(sp, BorderLayout.CENTER);
    }

    // ===== helper methods =====
    private GridBagConstraints gbc() {
        GridBagConstraints g = new GridBagConstraints();
        g.gridx=0; g.gridy=0; g.weightx=1; g.weighty=1;
        g.fill = GridBagConstraints.BOTH;
        return g;
    }

    private void styleTable(JTable t) {
        t.setRowHeight(28);
        t.setShowHorizontalLines(true);
        t.setShowVerticalLines(false);
        t.setGridColor(new Color(90,90,90));
        t.setForeground(new Color(240,240,240));
        t.setBackground(new Color(54,54,54));
        t.setSelectionBackground(new Color(70,70,70));
        t.setSelectionForeground(Color.WHITE);
        t.setIntercellSpacing(new Dimension(0, 1));

        JTableHeader hd = t.getTableHeader();
        hd.setPreferredSize(new Dimension(hd.getPreferredSize().width, 28));
        hd.setBackground(new Color(210,210,210));   // หัวตารางเทาอ่อน
        hd.setForeground(new Color(70,70,70));
        hd.setFont(hd.getFont().deriveFont(Font.BOLD, 13f));
        ((JComponent) hd).setBorder(
            BorderFactory.createMatteBorder(0,0,1,0,new Color(120,120,120)));
    }
}
