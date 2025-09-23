/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package UIAdmin;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.logging.Logger;

/**
 * Admin Dashboard (JFrame แบบเดิม) + หัวสไตล์ตามภาพ
 */
public class AdminDashboard extends javax.swing.JFrame {

    private static final Logger logger = Logger.getLogger(AdminDashboard.class.getName());

    // ====== ตัวแปร NetBeans ======
    private javax.swing.ButtonGroup buttonGroup1; // ยังปล่อยไว้ได้ แต่จะไม่ใช้แล้ว
    private javax.swing.JPanel jPanel1;  // header container (เดิม)
    private javax.swing.JPanel jPanel2;  // content (CardLayout)

    // ====== ปุ่มหัวแบบ pill ======
    private PillButton bookingBtn;
    private PillButton seatCloseBtn;
    private PillButton pricingBtn;
    private PillButton customerBtn;
    private PillButton leaveBtn;
    private JLabel hiLabel;
    private JLabel titleLabel;

    // สีตามภาพ
    private final Color BG_DARK = new Color(45,45,45);
    private final Color TAB_SELECTED_BG = new Color(248,245,242);  // ครีม
    private final Color TAB_SELECTED_TX = new Color(60,60,60);
    private final Color TAB_BG = new Color(243,144,47);            // ส้ม
    private final Color TAB_HOVER_BG = new Color(255,159,68);
    private final Color LEAVE_BG = new Color(110,110,110);
    private final Color LEAVE_HOVER = new Color(130,130,130);

    public AdminDashboard() {
        initComponents();
        // กำหนดขนาดหน้าหลัก
    setSize(512, 576);
    setPreferredSize(new Dimension(512, 576));


        // พื้นหลังทั้งเฟรม
        getContentPane().setBackground(BG_DARK);

        // --- สร้างหัวใหม่ลงใน jPanel1 (ยังคงเป็นส่วนหัวเดิม) ---
        jPanel1.setOpaque(false);
        jPanel1.setLayout(new BorderLayout());
        jPanel1.removeAll();

        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 6));
        left.setOpaque(false);

        titleLabel = new JLabel("Admin Dashboard");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 16f));
        left.add(titleLabel);
        left.add(Box.createHorizontalStrut(12));

        bookingBtn   = new PillButton("Booking", 18);
        seatCloseBtn = new PillButton("Seating/Closed Day", 18);
        pricingBtn   = new PillButton("Setting", 18);
        customerBtn  = new PillButton("Customer", 18);

        styleSelected(bookingBtn);
        styleUnselected(seatCloseBtn);
        styleUnselected(pricingBtn);
        styleUnselected(customerBtn);

        left.add(bookingBtn);
        left.add(seatCloseBtn);
        left.add(pricingBtn);
        left.add(customerBtn);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 6));
        right.setOpaque(false);
        hiLabel = new JLabel("Hi!  Admin");
        hiLabel.setForeground(Color.WHITE);
        hiLabel.setFont(hiLabel.getFont().deriveFont(14f));
        leaveBtn = new PillButton("Leave", 18);
        leaveBtn.setBackground(LEAVE_BG);
        leaveBtn.setForeground(Color.WHITE);
        leaveBtn.setHoverBackground(LEAVE_HOVER);
        leaveBtn.setBorder(new EmptyBorder(6,14,6,14));
        right.add(hiLabel);
        right.add(leaveBtn);

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setBorder(new EmptyBorder(8,12,4,12));
        header.add(left, BorderLayout.WEST);
        header.add(right, BorderLayout.EAST);

        jPanel1.add(header, BorderLayout.CENTER);

        // --- เนื้อหา: CardLayout + หน้าย่อยของคุณ ---
        jPanel2.setLayout(new CardLayout());
        jPanel2.add("booking", new AdminBooking());
        jPanel2.add("pricing", new PricingPanel());
        jPanel2.add("customerAccounts", new CustomerAccountsPanel());
        jPanel2.add("SeatContent", new SeatCloseContent());

        // --- ผูกการสลับหน้า + เปลี่ยนสถานะแท็บ ---
        CardLayout cl = (CardLayout) jPanel2.getLayout();
        bookingBtn.addActionListener(e -> { cl.show(jPanel2, "booking");          setSelected(bookingBtn);   });
        seatCloseBtn.addActionListener(e -> { cl.show(jPanel2, "SeatContent");     setSelected(seatCloseBtn); });
        pricingBtn.addActionListener(e -> { cl.show(jPanel2, "pricing");           setSelected(pricingBtn);   });
        customerBtn.addActionListener(e -> { cl.show(jPanel2, "customerAccounts"); setSelected(customerBtn);  });
        leaveBtn.addActionListener(e -> dispose()); // หรือแสดง dialog ยืนยันก็ได้
    }

    // ====== NetBeans initComponents (minimal, เก็บ jPanel1/jPanel2 ไว้) ======
    @SuppressWarnings("unchecked")
    private void initComponents() {
        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Admin Dashboard");
        setMinimumSize(new java.awt.Dimension(780, 520));

        // header container
        jPanel1.setBackground(new java.awt.Color(204, 204, 204));
        jPanel1.setPreferredSize(new java.awt.Dimension(700, 72));

        // content container
        jPanel2.setPreferredSize(new java.awt.Dimension(700, 480));
        jPanel2.setLayout(new java.awt.CardLayout());

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, 0)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }

    // ====== styling helpers ======
    private void setSelected(PillButton selected) {
        PillButton[] all = {bookingBtn, seatCloseBtn, pricingBtn, customerBtn};
        for (PillButton b : all) {
            if (b == selected) styleSelected(b); else styleUnselected(b);
            b.repaint();
        }
    }
    private void styleSelected(PillButton b) {
        b.setBackground(TAB_SELECTED_BG);
        b.setForeground(TAB_SELECTED_TX);
        b.setHoverBackground(TAB_SELECTED_BG);
        b.setBorder(new EmptyBorder(8,14,8,14));
    }
    private void styleUnselected(PillButton b) {
        b.setBackground(TAB_BG);
        b.setForeground(Color.WHITE);
        b.setHoverBackground(TAB_HOVER_BG);
        b.setBorder(new EmptyBorder(8,14,8,14));
    }

    // ====== ปุ่ม pill วาดเอง (อยู่ใน JFrame เดียว, ไม่ต้องสร้างคลาสแยก) ======
    static class PillButton extends JButton {
        private final int radius;
        private Color hoverBg;
        PillButton(String text, int radius) {
            super(text);
            this.radius = radius;
            setFocusPainted(false);
            setContentAreaFilled(false);
            setOpaque(false);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        }
        void setHoverBackground(Color c) { this.hoverBg = c; }
        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            Color bg = getModel().isRollover() && hoverBg != null ? hoverBg : getBackground();
            g2.setColor(bg);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius*2, radius*2);

            g2.setColor(getForeground());
            FontMetrics fm = g2.getFontMetrics();
            int tx = (getWidth() - fm.stringWidth(getText())) / 2;
            int ty = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
            g2.drawString(getText(), tx, ty);
            g2.dispose();
        }
    }

    // ====== main ======
    public static void main(String[] args) {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) { UIManager.setLookAndFeel(info.getClassName()); break; }
            }
        } catch (ReflectiveOperationException | UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        EventQueue.invokeLater(() -> new AdminDashboard().setVisible(true));
    }
}
