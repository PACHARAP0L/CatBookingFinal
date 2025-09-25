import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class RegisterFrame extends JFrame {

    // ---- Palette ให้ใกล้ภาพ ----
    private static final Color ORANGE_BORDER = new Color(255, 153, 0);
    private static final Color DARK_BG       = new Color(90, 90, 92);   // พื้นเทาเข้มใหญ่
    private static final Color CARD_BG       = new Color(110,110,112);  // เทาการ์ดกลาง
    private static final Color FIELD_BG      = Color.WHITE;
    private static final Color LABEL_GRAY    = new Color(220, 220, 220);
    private static final Color BTN_YELLOW    = new Color(246, 187, 37);
    private static final Color BTN_GRAY      = new Color(160,160,160);

    private final JTextField usernameField = new JTextField("yourusername");        // << NEW
    private final JTextField emailField    = new JTextField("you@email.com");
    private final JTextField phoneField    = new JTextField("081132xxxx");
    private final JPasswordField pwField   = new JPasswordField("At least 6 characters");
    private final JPasswordField cfField   = new JPasswordField("Re-enter password");
    private char pwEcho, cfEcho;

    public RegisterFrame() {
        super("Register");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(512, 576));
        setSize(512, 576);
        setLocationRelativeTo(null);

        // ขอบส้มรอบนอก
        JPanel border = new JPanel(new BorderLayout());
        border.setBackground(ORANGE_BORDER);
        border.setBorder(new EmptyBorder(10,10,10,10));
        setContentPane(border);

        // พื้นเทาเข้มด้านใน (ลด top padding เพื่อดันปุ่ม Login/Register ขึ้น)
        JPanel dark = new JPanel(new GridBagLayout());
        dark.setBackground(DARK_BG);
        dark.setBorder(new EmptyBorder(10,18,14,18)); // < ลดจาก 18,18,18,18
        border.add(dark, BorderLayout.CENTER);

        // การ์ดตรงกลาง (สีเทา) — ลดขอบบนเพื่อดันปุ่มขึ้น
        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(CARD_BG);
        card.setBorder(new EmptyBorder(12,24,18,24)); // < เดิม 22,24,22,24

        GridBagConstraints outer = new GridBagConstraints();
        outer.gridx=0; outer.gridy=0; outer.weightx=1; outer.weighty=1;
        outer.fill=GridBagConstraints.BOTH;
        dark.add(card, outer);

        GridBagConstraints g = new GridBagConstraints();
        g.gridx=0; g.gridy=0; g.insets = new Insets(2,6,2,6);
        g.anchor = GridBagConstraints.WEST;

        // แถบปุ่ม Login / Register (สไตล์เทาไล่สี)
        JPanel tabs = new JPanel(new FlowLayout(FlowLayout.LEFT, 22, 0));
        tabs.setOpaque(false);
        GradientButton loginTab = new GradientButton("Login");
        loginTab.setPreferredSize(new Dimension(120, 40));
        GradientButton registerTab = new GradientButton("Register");
        registerTab.setPreferredSize(new Dimension(120, 40));
        registerTab.setEnabled(false); // สไตล์ทึบกว่าเล็กน้อย
        tabs.add(loginTab);
        tabs.add(registerTab);

        g.gridwidth=2;
        card.add(tabs, g);

        // ========= Username (NEW) =========
        g.gridy++; g.gridwidth=2; g.insets = new Insets(8,6,2,6);
        card.add(label("Username"), g);

        g.gridy++; g.fill=GridBagConstraints.HORIZONTAL; g.insets = new Insets(0,6,8,6);
        styleText(usernameField);
        card.add(usernameField, g);

        // ========= Email =========
        g.gridy++; g.gridwidth=2; g.insets = new Insets(0,6,2,6);
        card.add(label("Email ( Must be @ )"), g);

        g.gridy++; g.fill=GridBagConstraints.HORIZONTAL; g.insets = new Insets(0,6,8,6);
        styleText(emailField);
        card.add(emailField, g);

        // ========= Phone =========
        g.gridy++; g.fill=GridBagConstraints.NONE; g.insets = new Insets(0,6,2,6);
        card.add(label("Phone ( Must be number )"), g);

        g.gridy++; g.fill=GridBagConstraints.HORIZONTAL; g.insets = new Insets(0,6,10,6);
        styleText(phoneField);
        ((AbstractDocument) phoneField.getDocument()).setDocumentFilter(new DigitFilterAllowDash());
        card.add(phoneField, g);

        // ========= Password =========
        g.gridy++; g.fill=GridBagConstraints.NONE; g.insets = new Insets(0,6,2,6);
        card.add(label("Password"), g);

        g.gridy++; g.gridwidth=2; g.fill=GridBagConstraints.HORIZONTAL; g.insets = new Insets(0,6,8,6);
        JPanel pwRow = new JPanel(new GridBagLayout());
        pwRow.setOpaque(false);
        GridBagConstraints pwc = new GridBagConstraints();
        pwc.gridx=0; pwc.gridy=0; pwc.weightx=1; pwc.fill=GridBagConstraints.HORIZONTAL;

        stylePassword(pwField);
        pwEcho = pwField.getEchoChar();
        makePlaceholder(pwField, "At least 6 characters");
        pwRow.add(pwField, pwc);

        pwc.gridx=1; pwc.weightx=0; pwc.insets=new Insets(0,8,0,0);
        GradientButton showPw = smallGradient("Show");
        showPw.addActionListener(e -> toggleEcho(pwField, showPw, pwEcho));
        pwRow.add(showPw, pwc);
        card.add(pwRow, g);

        // ========= Confirm Password =========
        g.gridy++; g.fill=GridBagConstraints.NONE; g.insets = new Insets(0,6,2,6);
        card.add(label("Comfirm Password"), g);

        g.gridy++; g.gridwidth=2; g.fill=GridBagConstraints.HORIZONTAL; g.insets = new Insets(0,6,8,6);
        JPanel cfRow = new JPanel(new GridBagLayout());
        cfRow.setOpaque(false);
        GridBagConstraints cfc = new GridBagConstraints();
        cfc.gridx=0; cfc.gridy=0; cfc.weightx=1; cfc.fill=GridBagConstraints.HORIZONTAL;

        stylePassword(cfField);
        cfEcho = cfField.getEchoChar();
        makePlaceholder(cfField, "Re-enter password");
        cfRow.add(cfField, cfc);

        cfc.gridx=1; cfc.weightx=0; cfc.insets=new Insets(0,8,0,0);
        GradientButton showCf = smallGradient("Show");
        showCf.addActionListener(e -> toggleEcho(cfField, showCf, cfEcho));
        cfRow.add(showCf, cfc);
        card.add(cfRow, g);

        // ========= ปุ่ม Register / Clear =========
        g.gridy++; g.gridwidth=1; g.insets=new Insets(16,6,0,6); g.fill=GridBagConstraints.NONE;
        JButton btnRegister = new JButton("Register");
        btnRegister.setBackground(BTN_YELLOW);
        btnRegister.setFocusPainted(false);
        btnRegister.setPreferredSize(new Dimension(140, 44));
        btnRegister.setFont(btnRegister.getFont().deriveFont(Font.BOLD, 16f));
        card.add(btnRegister, g);

        g.gridx=1;
        JButton btnClear = new JButton("Clear");
        btnClear.setBackground(BTN_GRAY);
        btnClear.setForeground(Color.DARK_GRAY);
        btnClear.setFocusPainted(false);
        btnClear.setPreferredSize(new Dimension(140, 44));
        btnClear.setFont(btnClear.getFont().deriveFont(Font.BOLD, 16f));
        card.add(btnClear, g);

        // Actions
        btnClear.addActionListener(e -> {
            usernameField.setText("");
            emailField.setText("");
            phoneField.setText("");
            pwField.setText("");
            cfField.setText("");
            usernameField.requestFocus();
        });
        loginTab.addActionListener(e ->
            { new LoginFrame().setVisible(true); this.dispose(); }
        );
        btnRegister.addActionListener(e ->
            JOptionPane.showMessageDialog(this, "Register clicked with username: " + usernameField.getText())
        );
    }

    private JLabel label(String text){
        JLabel l = new JLabel(text);
        l.setForeground(LABEL_GRAY);
        l.setFont(l.getFont().deriveFont(Font.PLAIN, 14f));
        return l;
    }
    private void styleText(JTextField f){
        f.setBackground(FIELD_BG);
        f.setForeground(Color.BLACK);
        f.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY, 2),
                new EmptyBorder(3,6,3,6)));
        f.setPreferredSize(new Dimension(10, 26));
    }
    private void stylePassword(JPasswordField f) {
        f.setBackground(FIELD_BG);
        f.setForeground(Color.BLACK);
        f.setColumns(24);
        f.setMinimumSize(new Dimension(220, 28));
        f.setPreferredSize(new Dimension(320, 28));
        f.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY, 2),
                new javax.swing.border.EmptyBorder(3, 6, 3, 6)
        ));
        f.setEchoChar((char) 0); // เพื่อให้เห็น placeholder; โฟกัสแล้วค่อยเปลี่ยนเป็น '•'
    }
    private void makePlaceholder(JTextComponent field, String hint){
        Color hintColor = new Color(140,140,140);
        Color normal = Color.BLACK;
        field.setText(hint);
        field.setForeground(hintColor);
        field.addFocusListener(new FocusAdapter() {
            @Override public void focusGained(FocusEvent e) {
                if (field.getText().equals(hint)) {
                    field.setText("");
                    field.setForeground(normal);
                    if (field instanceof JPasswordField pw) pw.setEchoChar('\u2022');
                }
            }
            @Override public void focusLost(FocusEvent e) {
                if (field.getText().isEmpty()) {
                    field.setText(hint);
                    field.setForeground(hintColor);
                    if (field instanceof JPasswordField pw) pw.setEchoChar((char)0);
                }
            }
        });
    }
    private void toggleEcho(JPasswordField field, JButton btn, char original){
        boolean showing = field.getEchoChar()==0;
        if (showing) { field.setEchoChar(original); btn.setText("Show"); }
        else { field.setEchoChar((char)0); btn.setText("Hide"); }
    }

    // ปุ่มไล่เฉดเทา (เหมือนในภาพ)
    static class GradientButton extends JButton {
        GradientButton(String text){ super(text); setFocusPainted(false); setContentAreaFilled(false); }
        @Override protected void paintComponent(Graphics g){
            Graphics2D g2=(Graphics2D)g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int w=getWidth(), h=getHeight();
            GradientPaint gp = new GradientPaint(0,0, new Color(230,230,230),
                                                 0,h, new Color(180,180,185));
            g2.setPaint(gp);
            g2.fillRoundRect(0,0,w,h,8,8);
            super.paintComponent(g2);
            g2.dispose();
        }
    }
    private GradientButton smallGradient(String text){
        GradientButton b = new GradientButton(text);
        b.setPreferredSize(new Dimension(90, 30));
        b.setFont(b.getFont().deriveFont(Font.BOLD, 13f));
        return b;
    }

    // รับเฉพาะตัวเลข/ขีด (ช่วยให้พิมพ์เบอร์ได้)
    static class DigitFilterAllowDash extends DocumentFilter {
        @Override public void insertString(FilterBypass fb, int off, String s, AttributeSet a) throws BadLocationException {
            if (s!=null && s.matches("[0-9-]+")) super.insertString(fb, off, s, a);
        }
        @Override public void replace(FilterBypass fb, int off, int len, String s, AttributeSet a) throws BadLocationException {
            if (s==null || s.matches("[0-9-]*")) super.replace(fb, off, len, s, a);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new RegisterFrame().setVisible(true));
    }
}
