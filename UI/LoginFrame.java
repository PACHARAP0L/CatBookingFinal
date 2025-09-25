import javax.swing.*;
import javax.swing.border.EmptyBorder;

import Core.Domain.User;

import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class LoginFrame extends JFrame {

    // --- สีหลักให้ใกล้เคียงภาพ ---
    private static final Color ORANGE_BG   = new Color(242, 142, 38);
    private static final Color CHARCOAL    = new Color(51, 51, 51);
    private static final Color PEACH       = new Color(255, 221, 193);
    private static final Color FIELD_BG    = new Color(230, 233, 236);
    private static final Color LABEL_WHITE = new Color(245, 245, 245);

    private final JPasswordField passwordField = new JPasswordField();
    private final JTextField emailField = new JTextField();
    private final JButton showBtn = new RoundedButton("Show", 12);

    private char defaultEcho;

    public LoginFrame() {
        super("CatPlaytime Booking - Login");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(512,576);             // ขนาดใกล้เคียงภาพ
        setLocationRelativeTo(null);

        // พื้นหลังส้ม + ขอบเข้มบางๆรอบนอก
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(CHARCOAL);
        root.setBorder(new EmptyBorder(8, 8, 8, 8));

        JPanel orange = new JPanel();
        orange.setBackground(ORANGE_BG);
        orange.setLayout(new BorderLayout());
        orange.setBorder(new EmptyBorder(16, 16, 16, 16));
        root.add(orange, BorderLayout.CENTER);

        // หัวเรื่อง 2 บรรทัด
        JPanel titleWrap = new JPanel();
        titleWrap.setOpaque(false);
        titleWrap.setLayout(new BoxLayout(titleWrap, BoxLayout.Y_AXIS));

        JLabel line1 = new JLabel("Welcome to");
        line1.setFont(new Font("Georgia", Font.BOLD, 38));
        line1.setForeground(CHARCOAL);
        line1.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel line2 = new JLabel("CatPlaytime Booking");
        line2.setFont(new Font("Georgia", Font.BOLD, 36));
        line2.setForeground(CHARCOAL);
        line2.setAlignmentX(Component.CENTER_ALIGNMENT);

        titleWrap.add(line1);
        titleWrap.add(Box.createVerticalStrut(4));
        titleWrap.add(line2);
        orange.add(titleWrap, BorderLayout.NORTH);

        // การ์ดตรงกลางสีเทาเข้มมุมโค้ง
        RoundedPanel card = new RoundedPanel(20, CHARCOAL);
        card.setLayout(new GridBagLayout());
        card.setBorder(new EmptyBorder(18, 24, 18, 24));

        GridBagConstraints gc = new GridBagConstraints();
        gc.gridx = 0; gc.gridy = 0; gc.gridwidth = 2;
        gc.anchor = GridBagConstraints.CENTER;
        gc.insets = new Insets(0, 0, 12, 0);

        // แถวปุ่ม Login / Register
        JPanel topButtons = new JPanel();
        topButtons.setOpaque(false);
        topButtons.setLayout(new FlowLayout(FlowLayout.CENTER, 36, 0));

        JButton btnLogin = makePeachButton("Login");
        JButton btnRegister = makePeachButton("Register");
        topButtons.add(btnLogin);
        topButtons.add(btnRegister);

        card.add(topButtons, gc);

        // ฟอร์มอีเมล + พาสเวิร์ด
        gc.gridwidth = 1;
        gc.gridy++;
        gc.anchor = GridBagConstraints.WEST;
        gc.insets = new Insets(6, 4, 4, 4);
        JLabel emailLabel = new JLabel("Email");
        emailLabel.setForeground(LABEL_WHITE);
        emailLabel.setFont(new Font("Georgia", Font.BOLD, 16));
        card.add(emailLabel, gc);

        gc.gridy++;
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.weightx = 1.0;
        emailField.setBackground(FIELD_BG);
        emailField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(4, 8, 4, 8),
                BorderFactory.createLineBorder(new Color(0,0,0,0), 0)));
        emailField.setPreferredSize(new Dimension(320, 32));
        installPlaceholder(emailField, "you@email.com");
        card.add(emailField, gc);

        gc.gridy++;
        gc.fill = GridBagConstraints.NONE;
        gc.weightx = 0;
        JLabel pwLabel = new JLabel("Password");
        pwLabel.setForeground(LABEL_WHITE);
        pwLabel.setFont(new Font("Georgia", Font.BOLD, 16));
        card.add(pwLabel, gc);

        // ช่องพาส + ปุ่ม Show อยู่บรรทัดเดียวกัน
        gc.gridy++;
        JPanel pwRow = new JPanel(new GridBagLayout());
        pwRow.setOpaque(false);

        GridBagConstraints pwc = new GridBagConstraints();
        pwc.gridx = 0; pwc.gridy = 0;
        pwc.weightx = 1.0; pwc.fill = GridBagConstraints.HORIZONTAL;

        passwordField.setBackground(FIELD_BG);
        passwordField.setPreferredSize(new Dimension(280, 32));
        passwordField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(4, 8, 4, 8),
                BorderFactory.createLineBorder(new Color(0,0,0,0), 0)));
        defaultEcho = passwordField.getEchoChar();
        pwRow.add(passwordField, pwc);

        pwc.gridx = 1; pwc.weightx = 0; pwc.insets = new Insets(0, 8, 0, 0);
        showBtn.setBackground(PEACH);
        showBtn.setFocusPainted(false);
        showBtn.setBorder(new EmptyBorder(6, 14, 6, 14));
        showBtn.addActionListener(e -> togglePassword());
        pwRow.add(showBtn, pwc);

        gc.fill = GridBagConstraints.HORIZONTAL;
        card.add(pwRow, gc);

        // เพิ่มการ์ดลงกึ่งกลางพื้นส้ม
        JPanel centerWrap = new JPanel(new GridBagLayout());
        centerWrap.setOpaque(false);
        centerWrap.add(card);
        orange.add(centerWrap, BorderLayout.CENTER);

        setContentPane(root);

        // ตัวอย่าง event (คุณผูกลอจิกจริงภายหลังได้)
        btnLogin.addActionListener(e -> {
    String email = emailField.getText().trim();
    String pw    = new String(passwordField.getPassword());

    new CelenDerPage().setVisible(true);  // ไปหน้าปฏิทิน
    dispose();                             // ปิดหน้า Login
});


        btnRegister.addActionListener(e ->  {                       
    // เปิดหน้า RegisterForm
    javax.swing.SwingUtilities.invokeLater(() -> {           
        new RegisterFrame().setVisible(true);                 
    });
    // ปิดหน้า Login 
    this.dispose();                                          
});
    }

    private JButton makePeachButton(String text) {
        JButton b = new RoundedButton(text, 16);
        b.setBackground(PEACH);
        b.setForeground(CHARCOAL);
        b.setFont(new Font("Georgia", Font.BOLD, 16));
        b.setFocusPainted(false);
        b.setBorder(new EmptyBorder(8, 20, 8, 20));
        return b;
    }

    private void togglePassword() {
        boolean showing = passwordField.getEchoChar() == 0;
        if (showing) {
            passwordField.setEchoChar(defaultEcho);
            showBtn.setText("Show");
        } else {
            passwordField.setEchoChar((char) 0);
            showBtn.setText("Hide");
        }
    }

    // ใส่ placeholder แบบง่ายสำหรับ JTextField
    private void installPlaceholder(JTextField field, String placeholder) {
        Color hint = new Color(150, 150, 150);
        Color normal = Color.BLACK;
        field.setText(placeholder);
        field.setForeground(hint);

        field.addFocusListener(new FocusAdapter() {
            @Override public void focusGained(FocusEvent e) {
                if (field.getText().equals(placeholder)) {
                    field.setText("");
                    field.setForeground(normal);
                }
            }
            @Override public void focusLost(FocusEvent e) {
                if (field.getText().isEmpty()) {
                    field.setText(placeholder);
                    field.setForeground(hint);
                }
            }
        });
    }

    // --- แผงมุมโค้งวาดเอง ---
    static class RoundedPanel extends JPanel {
        private final int radius;
        private final Color bg;

        RoundedPanel(int radius, Color bg) {
            this.radius = radius;
            this.bg = bg;
            setOpaque(false);
        }

        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(bg);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius*2, radius*2);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    // --- ปุ่มมุมโค้งพื้นทึบ ---
    static class RoundedButton extends JButton {
        private final int radius;
        RoundedButton(String text, int radius) {
            super(text);
            this.radius = radius;
            setContentAreaFilled(false);
            setOpaque(false);
        }
        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius*2, radius*2);
            super.paintComponent(g2);
            g2.dispose();
        }
        @Override public void updateUI() {
            super.updateUI();
            setForeground(Color.DARK_GRAY);
            setBorderPainted(false);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
    }
}
