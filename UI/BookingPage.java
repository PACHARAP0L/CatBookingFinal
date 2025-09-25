
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.text.DecimalFormat;

public class BookingPage extends JFrame {

    // Palette
    private static final Color ORANGE    = new Color(242, 142, 38);
    private static final Color CHARCOAL  = new Color(43, 48, 53);
    private static final Color PANEL_DARK= new Color(60, 66, 72);
    private static final Color PANEL     = new Color(67, 74, 81);
    private static final Color FIELD     = new Color(82, 90, 98);
    private static final Color TEXT_LITE = new Color(230, 235, 240);
    private static final Color TEXT_DIM  = new Color(185, 192, 199);
    private static final Color OK_COLOR  = new Color(255, 140, 20);
    private static final Color CLEAR_BG  = new Color(120, 126, 133);

    private final JComboBox<String> cbStart = new JComboBox<>(new String[]{
        "09:00–10:00","10:00–11:00","11:00–12:00","12:00–13:00","14:00–15:00","15:00–16:00"
    });
    private final JComboBox<String> cbDuration = new JComboBox<>(new String[]{"1","2"});
    private final JTextField tfDate = new JTextField(""+(java.time.LocalDate.now().plusDays(1)));
    private final JTextField tfNote = new JTextField();
    private final JLabel lblCount = new JLabel("1", SwingConstants.CENTER);
    private final JLabel lblTotal = new JLabel("$ 100.00");
    private int people = 1;

    public BookingPage() {
        super("Booking");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(512, 576));
        setSize(512, 576);
        setLocationRelativeTo(null);

        JPanel border = new JPanel(new BorderLayout());
        border.setBackground(ORANGE);
        border.setBorder(new EmptyBorder(8,8,8,8)); // orange border like screenshot
        setContentPane(border);

        RoundedPanel card = new RoundedPanel(18, PANEL_DARK);
        card.setLayout(new GridBagLayout());
        card.setBorder(new EmptyBorder(12,12,12,12));
        border.add(card, BorderLayout.CENTER);

        // Left form & Right rule
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(0,0,0,0);
        c.gridx=0; c.gridy=0; c.weightx=1; c.weighty=1; c.fill=GridBagConstraints.BOTH;

        JPanel form = buildFormPanel();
        JPanel rule = buildRulePanel();

        card.add(form, c);
        c.gridx=1; c.weightx=0.42; // narrower right
        c.insets = new Insets(0,10,0,0);
        card.add(rule, c);
    }

    private JPanel buildRulePanel() {
        RoundedPanel p = new RoundedPanel(12, PANEL);
        p.setLayout(new GridBagLayout());
        p.setBorder(new EmptyBorder(12,12,12,12));

        JLabel title = new JLabel("Rule");
        title.setFont(title.getFont().deriveFont(Font.BOLD, 16f));
        title.setForeground(TEXT_LITE);

        JLabel list = new JLabel("<html><ul style='margin:6 0 0 16;padding:0'>"
                + "<li>Wash hands before playing</li>"
                + "<li>No flash/loud noises</li>"
                + "</ul></html>");
        list.setForeground(TEXT_LITE);
        list.setFont(list.getFont().deriveFont(13f));

        GridBagConstraints g = new GridBagConstraints();
        g.gridx=0; g.gridy=0; g.anchor=GridBagConstraints.NORTHWEST;
        g.insets = new Insets(0,0,6,0);
        p.add(title, g);
        g.gridy=1; g.weighty=1; g.weightx=1; g.fill=GridBagConstraints.BOTH;
        p.add(list, g);
        return p;
    }

    private JPanel buildFormPanel() {
        RoundedPanel p = new RoundedPanel(12, PANEL);
        p.setBorder(new EmptyBorder(12,12,12,12));
        p.setLayout(new GridBagLayout());

        GridBagConstraints g = new GridBagConstraints();
        g.gridx=0; g.gridy=0; g.gridwidth=4; g.anchor=GridBagConstraints.WEST;
        g.insets = new Insets(0,0,8,0);

        JLabel title = new JLabel("Book an appointment to play with the cat");
        title.setFont(title.getFont().deriveFont(Font.BOLD, 16f));
        title.setForeground(TEXT_LITE);
        p.add(title, g);

        // Date
        g.gridy++; g.gridwidth=4; g.insets = new Insets(2,0,2,0);
        JLabel lbDate = makeLabel("Date (YYYY-MM-DD)");
        p.add(lbDate, g);

        g.gridy++; g.gridwidth=4; g.fill=GridBagConstraints.HORIZONTAL;
        JPanel dateRow = new JPanel(new BorderLayout(6,0));
        dateRow.setOpaque(false);
        styleField(tfDate);
        JButton calendar = smallIconButton("\uD83D\uDCC5"); // calendar emoji
        dateRow.add(tfDate, BorderLayout.CENTER);
        dateRow.add(calendar, BorderLayout.EAST);
        p.add(dateRow, g);

        calendar.addActionListener(e -> {
            new CelenDerPage().setVisible(true);
            dispose();
            });

        // Separator line
        g.gridy++; g.gridwidth=4; g.insets = new Insets(8,0,10,0);
        JSeparator sep = new JSeparator(SwingConstants.HORIZONTAL);
        sep.setForeground(Color.BLACK);
        p.add(sep, g);

        // Start time & Duration
        g.gridy++; g.gridwidth=2; g.insets = new Insets(0,0,4,8);
        p.add(makeLabel("Start time"), g);
        g.gridx=2; g.insets = new Insets(0,0,4,0);
        p.add(makeLabel("Duration (hours)"), g);

        g.gridy++; g.gridx=0; g.fill=GridBagConstraints.HORIZONTAL; g.insets = new Insets(0,0,10,8);
        styleCombo(cbStart);
        p.add(cbStart, g);
        g.gridx=2; g.insets = new Insets(0,0,10,0);
        styleCombo(cbDuration);
        p.add(cbDuration, g);

        // Remaining slots
        g.gridx=0; g.gridy++; g.gridwidth=4; g.insets = new Insets(0,0,6,0);
        p.add(makeLabel("Remaining by slot (today)"), g);

        // grid of 6 slot tiles
        g.gridy++; g.gridwidth=4; g.fill=GridBagConstraints.HORIZONTAL; g.insets = new Insets(0,0,0,0);
        JPanel grid = new JPanel(new GridLayout(2,3,10,10));
        grid.setOpaque(false);
        String[] times = {"09:00–10:00","10:00–11:00","11:00–12:00","12:00–13:00","14:00–15:00","15:00–16:00"};
        for (String t : times) grid.add(makeSlot(t, "Remaining 10"));
        p.add(grid, g);

        // People
        g.gridy++; g.gridwidth=4; g.insets = new Insets(10,0,0,0); g.fill=GridBagConstraints.NONE; g.anchor=GridBagConstraints.WEST;
        p.add(makeLabel("Number of people"), g);

        g.gridy++; g.insets = new Insets(6,0,0,0);
        JPanel peopleRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        peopleRow.setOpaque(false);

        JButton minus = circleButton("-");
        JButton plus  = circleButton("+");
        styleCountLabel(lblCount);

        minus.addActionListener(e -> { if (people>1){ people--; updateTotal(); }});
        plus.addActionListener(e -> { if (people<10){ people++; updateTotal(); }});

        peopleRow.add(minus);
        peopleRow.add(lblCount);
        peopleRow.add(plus);
        p.add(peopleRow, g);

        // Note
        g.gridy++; g.gridwidth=4; g.insets = new Insets(10,0,2,0); g.anchor=GridBagConstraints.WEST;
        p.add(makeLabel("Note"), g);

        g.gridy++; g.fill=GridBagConstraints.HORIZONTAL; g.insets = new Insets(0,0,10,0);
        styleField(tfNote);
        tfNote.setToolTipText("For example, I want to meet the fluffy cat.");
        p.add(tfNote, g);

        // Footer: total + buttons
        g.gridy++; g.gridwidth=4; g.insets = new Insets(8,0,0,0); g.fill=GridBagConstraints.HORIZONTAL;
        RoundedPanel footer = new RoundedPanel(10, PANEL_DARK);
        footer.setLayout(new GridBagLayout());
        footer.setBorder(new EmptyBorder(8,10,8,10));

        GridBagConstraints f = new GridBagConstraints();
        f.gridx=0; f.gridy=0; f.weightx=1; f.anchor=GridBagConstraints.WEST;
        JLabel totalText = new JLabel("Total:");
        totalText.setForeground(TEXT_LITE);
        totalText.setFont(totalText.getFont().deriveFont(Font.BOLD, 14f));
        footer.add(totalText, f);

        f.gridx=1; f.weightx=1; f.anchor=GridBagConstraints.WEST;
        lblTotal.setForeground(TEXT_LITE);
        lblTotal.setFont(lblTotal.getFont().deriveFont(Font.BOLD, 16f));
        footer.add(lblTotal, f);

        f.gridx=2; f.weightx=0; f.anchor=GridBagConstraints.EAST;
        JButton ok = new RoundedButton("Ok", 12);
        ok.setBackground(OK_COLOR);
        ok.setForeground(Color.BLACK);
        ok.setBorder(new EmptyBorder(8,16,8,16));
        ok.setFocusPainted(false);
        footer.add(ok, f);

        ok.addActionListener(e -> {
            new Bill().setVisible(true);
            dispose();
        });

        f.gridx=3; f.insets = new Insets(0,10,0,0);
        JButton clear = new RoundedButton("Clear", 12);
        clear.setBackground(CLEAR_BG);
        clear.setForeground(TEXT_LITE);
        clear.setBorder(new EmptyBorder(8,16,8,16));
        clear.setFocusPainted(false);
        clear.addActionListener(e -> clearForm());
        footer.add(clear, f);

        p.add(footer, g);

        return p;
    }

    private void updateTotal(){
        lblCount.setText(String.valueOf(people));
        // Simple pricing: $100 per booking + $0 per extra person (match screenshot)
        double total = 100.0;
        DecimalFormat df = new DecimalFormat("$ 0.00");
        lblTotal.setText(df.format(total));
    }

    private void clearForm(){
        tfDate.setText(""+(java.time.LocalDate.now().plusDays(1)));
        cbStart.setSelectedIndex(0);
        cbDuration.setSelectedIndex(0);
        people = 1;
        updateTotal();
        tfNote.setText("");
    }

    // --- UI helpers ---
    private JLabel makeLabel(String text){
        JLabel l = new JLabel(text);
        l.setForeground(TEXT_LITE);
        l.setFont(l.getFont().deriveFont(Font.BOLD, 13.5f));
        return l;
    }
    private void styleField(JTextField f){
        f.setBackground(FIELD);
        f.setForeground(TEXT_LITE);
        f.setCaretColor(TEXT_LITE);
        f.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(6,8,6,8),
            BorderFactory.createLineBorder(new Color(0,0,0,0), 0)
        ));
        f.setPreferredSize(new Dimension(10, 34));
    }
    private void styleCombo(JComboBox<?> cb){
        cb.setBackground(FIELD);
        cb.setForeground(TEXT_LITE);
        cb.setFocusable(false);
        cb.setBorder(new EmptyBorder(6,8,6,8));
        cb.setPreferredSize(new Dimension(10, 34));
    }
    private JButton smallIconButton(String text){
        JButton b = new JButton(text);
        b.setFont(new Font("Dialog", Font.PLAIN, 14));
        b.setBackground(FIELD.darker());
        b.setForeground(TEXT_LITE);
        b.setFocusPainted(false);
        b.setBorder(new EmptyBorder(6,10,6,10));
        return b;
    }
    private JButton circleButton(String text){
        JButton b = new JButton(text){
            @Override protected void paintComponent(Graphics g){
                Graphics2D g2=(Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(FIELD);
                g2.fillRoundRect(0,0,getWidth(),getHeight(), 16, 16);
                super.paintComponent(g2);
                g2.dispose();
            }
        };
        b.setForeground(TEXT_LITE);
        b.setFocusPainted(false);
        b.setBorder(new EmptyBorder(4,10,4,10));
        b.addActionListener(e -> {
            // handled externally by attaching to minus/plus
        });
        return b;
    }
    private void styleCountLabel(JLabel l){
        l.setOpaque(true);
        l.setBackground(FIELD);
        l.setForeground(TEXT_LITE);
        l.setBorder(new EmptyBorder(6,18,6,18));
    }
    private RoundedPanel makeSlot(String time, String remain){
        RoundedPanel tile = new RoundedPanel(12, PANEL_DARK);
        tile.setLayout(new BorderLayout());
        tile.setBorder(new EmptyBorder(10,12,10,12));
        JLabel t = new JLabel(time);
        t.setForeground(TEXT_LITE);
        t.setFont(t.getFont().deriveFont(Font.BOLD, 13.5f));
        JLabel r = new JLabel(remain);
        r.setForeground(TEXT_DIM);
        r.setFont(r.getFont().deriveFont(12f));
        tile.add(t, BorderLayout.NORTH);
        tile.add(r, BorderLayout.SOUTH);
        tile.setPreferredSize(new Dimension(160, 52));
        return tile;
    }

    // --- Custom rounded components ---
    static class RoundedPanel extends JPanel {
        private final int radius;
        private final Color bg;
        RoundedPanel(int r, Color c){
            radius = r; bg = c; setOpaque(false);
        }
        @Override protected void paintComponent(Graphics g){
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(bg);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius*2, radius*2);
            g2.dispose();
            super.paintComponent(g);
        }
    }
    static class RoundedButton extends JButton {
        private final int radius;
        RoundedButton(String text, int r){ super(text); radius=r; setContentAreaFilled(false); setOpaque(false); }
        @Override protected void paintComponent(Graphics g){
            Graphics2D g2=(Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0,0,getWidth(),getHeight(), radius*2, radius*2);
            super.paintComponent(g2);
            g2.dispose();
        }
        @Override public void updateUI(){ super.updateUI(); setBorderPainted(false); }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new BookingPage().setVisible(true));
    }
}
