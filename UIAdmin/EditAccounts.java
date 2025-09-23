package UIAdmin;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

public class EditAccounts extends JPanel {

    private static final Color LIGHT_TEXT = new Color(240,240,240);

    private JTextField nameField, emailField, roleField;
    private JPasswordField passwordField;
    private JButton saveBtn, deleteBtn;

    public interface SaveHandler { void onSave(String name, String email, String password, String role); }
    private SaveHandler saveHandler;
    private Runnable deleteHandler;

    public EditAccounts() {
        setOpaque(false);
        setLayout(new GridBagLayout());

        JLabel title = new JLabel("Customer details");
        title.setForeground(LIGHT_TEXT);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 14f));

        deleteBtn = new JButton("Delete customer");
        deleteBtn.setFocusPainted(false);
        deleteBtn.setContentAreaFilled(false);
        deleteBtn.setBorder(null);
        deleteBtn.setForeground(new Color(235, 160, 160));
        deleteBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        deleteBtn.addActionListener(e -> { if (deleteHandler != null) deleteHandler.run(); });

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.add(title, BorderLayout.WEST);
        header.add(deleteBtn, BorderLayout.EAST);

        nameField  = field();
        emailField = field();
        roleField  = field();
        passwordField = new JPasswordField();
        passwordField.setBorder(new CompoundBorder(
                new MatteBorder(1,1,1,1, new Color(200,200,200)),
                new EmptyBorder(8,12,8,12)
        ));

        saveBtn = pillGreen("Save");
        JButton cancelBtn = pillWhite("Cancel");
        cancelBtn.addActionListener(e -> passwordField.setText(""));

        saveBtn.addActionListener(e -> {
            if (saveHandler != null) {
                saveHandler.onSave(
                        nameField.getText(),
                        emailField.getText(),
                        new String(passwordField.getPassword()), // plain text
                        roleField.getText()
                );
                passwordField.setText("");
            }
        });

        int y=0;
        add(header,                              gbc(0,y++,1,1,1,0, new Insets(0,0,12,0)));
        add(labeled("Name", nameField),          gbc(0,y++,1,1,1,0, new Insets(0,0,8,0)));
        add(labeled("Email", emailField),        gbc(0,y++,1,1,1,0, new Insets(0,0,8,0)));
        add(labeled("Role", roleField),          gbc(0,y++,1,1,1,0, new Insets(0,0,8,0)));
        add(labeled("Password", passwordField),  gbc(0,y++,1,1,1,0, new Insets(0,0,12,0)));

        JPanel btns = new JPanel(new FlowLayout(FlowLayout.LEFT,10,0));
        btns.setOpaque(false);
        btns.add(saveBtn); btns.add(cancelBtn);
        add(btns,                               gbc(0,y,1,1,1,0, new Insets(0,0,0,0)));
    }

    public void bindUser(CustomerAccountsPanel.User u){
        if (u == null) return;
        nameField.setText(u.name);
        emailField.setText(u.email);
        roleField.setText(u.role);
        passwordField.setText(u.password); // plain text
    }

    public void setOnSave(SaveHandler h){ this.saveHandler = h; }
    public void setOnDelete(Runnable r){ this.deleteHandler = r; }

    // ===== helpers =====
    private GridBagConstraints gbc(int x,int y,int w,int h,double wx,double wy,Insets in){
        GridBagConstraints g = new GridBagConstraints();
        g.gridx=x; g.gridy=y; g.gridwidth=w; g.gridheight=h;
        g.weightx=wx; g.weighty=wy; g.insets=in;
        g.fill=GridBagConstraints.HORIZONTAL;
        return g;
    }
    private JPanel labeled(String label, JComponent field){
        JPanel p = new JPanel(new BorderLayout(0,4));
        p.setOpaque(false);
        JLabel lb = new JLabel(label);
        lb.setForeground(LIGHT_TEXT);
        p.add(lb, BorderLayout.NORTH);
        p.add(field, BorderLayout.CENTER);
        return p;
    }
    private JTextField field(){
        JTextField tf = new JTextField();
        tf.setBorder(new CompoundBorder(
                new MatteBorder(1,1,1,1, new Color(200,200,200)),
                new EmptyBorder(8,12,8,12)
        ));
        tf.setBackground(Color.WHITE);
        tf.setForeground(Color.DARK_GRAY);
        return tf;
    }
    private JButton pillGreen(String text){
        JButton b = new JButton(text){
            @Override protected void paintComponent(Graphics g){
                Graphics2D g2=(Graphics2D)g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(120, 230, 150));
                g2.fillRoundRect(0,0,getWidth(),getHeight(),22,22);
                g2.dispose(); super.paintComponent(g);
            }
        };
        b.setFocusPainted(false); b.setContentAreaFilled(false);
        b.setBorder(new EmptyBorder(6,18,6,18));
        b.setForeground(Color.DARK_GRAY);
        return b;
    }
    private JButton pillWhite(String text){
        JButton b = new JButton(text){
            @Override protected void paintComponent(Graphics g){
                Graphics2D g2=(Graphics2D)g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(245,245,245));
                g2.fillRoundRect(0,0,getWidth(),getHeight(),22,22);
                g2.dispose(); super.paintComponent(g);
            }
        };
        b.setFocusPainted(false); b.setContentAreaFilled(false);
        b.setBorder(new EmptyBorder(6,16,6,16));
        b.setForeground(Color.DARK_GRAY);
        return b;
    }
}
