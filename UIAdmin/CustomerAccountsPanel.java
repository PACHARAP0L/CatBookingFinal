package UIAdmin;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerAccountsPanel extends JPanel {

    private static final Color ORANGE = new Color(243,144,47);
    private static final Color DARK   = new Color(48,48,48);
    private static final Color LIGHT  = new Color(240,240,240);

    private static final String CSV_PATH = "Data/User.csv";
    private final List<User> users = new ArrayList<>();

    private DefaultListModel<User> listModel;
    private JList<User> customerList;
    private JTextField searchField;

    private EditAccounts editor;

    public CustomerAccountsPanel() {
        setPreferredSize(new Dimension(512, 506));
        setLayout(new BorderLayout());
        setOpaque(false);

        JPanel content = new JPanel(new BorderLayout());
        content.setBackground(DARK);
        content.setBorder(new MatteBorder(6,6,6,6, ORANGE));
        add(content, BorderLayout.CENTER);

        JPanel grid = new JPanel(new GridBagLayout());
        grid.setOpaque(false);
        grid.setBorder(new EmptyBorder(10, 12, 10, 12));
        content.add(grid, BorderLayout.CENTER);

        GridBagConstraints gcLeft  = gbc(0,0,1,1,0.58,1, new Insets(0,10,0,10));
        GridBagConstraints gcRight = gbc(1,0,1,1,0.42,1, new Insets(0,0,0,10));

        // ===== LEFT =====
        JPanel left = new JPanel(new GridBagLayout());
        left.setOpaque(false);

        JLabel leftTitle = title("All customers");
        JButton addBtn = pillOrange("+ Add Customer");
        addBtn.addActionListener(e -> {
            User u = new User("New User", "user"+(users.size()+1)+"@mail.com", "user", "1234");
            users.add(u);
            listModel.addElement(u);
            customerList.setSelectedIndex(listModel.size()-1);
            saveUsersToCSV(CSV_PATH);
        });

        JPanel leftHeader = new JPanel(new BorderLayout());
        leftHeader.setOpaque(false);
        leftHeader.add(leftTitle, BorderLayout.WEST);
        leftHeader.add(addBtn,   BorderLayout.EAST);

        searchField = roundedField();
        searchField.setToolTipText("Search by name or email");
        searchField.getDocument().addDocumentListener(CustomerAccountsPanel.SimpleDoc.onChange(() -> {
            String q = searchField.getText().trim().toLowerCase();
            filterList(q);
        }));

        listModel = new DefaultListModel<>();
        customerList = new JList<>(listModel);
        customerList.setBackground(new Color(58,58,58));
        customerList.setForeground(LIGHT);
        customerList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        customerList.setBorder(new CompoundBorder(
                new MatteBorder(1,1,1,1, new Color(80,80,80)),
                new EmptyBorder(6,8,6,8)
        ));
        customerList.setCellRenderer((list, value, index, isSelected, cellHasFocus) -> {
            JPanel p = new JPanel(new BorderLayout());
            p.setOpaque(true);
            p.setBackground(isSelected ? new Color(72,72,72) : new Color(58,58,58));
            p.setBorder(new EmptyBorder(4,6,4,6));
            JLabel top = new JLabel(value.name);
            top.setForeground(new Color(250,250,250));
            JLabel sub = new JLabel(value.email + "  · " + value.role);
            sub.setForeground(new Color(210,210,210));
            p.add(top, BorderLayout.NORTH);
            p.add(sub, BorderLayout.SOUTH);
            return p;
        });

        customerList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                User u = customerList.getSelectedValue();
                if (u != null) editor.bindUser(u);
            }
        });

        JScrollPane listScroll = new JScrollPane(customerList);
        listScroll.getViewport().setBackground(new Color(58,58,58));
        listScroll.setBorder(new MatteBorder(1,1,1,1, new Color(80,80,80)));

        left.add(leftHeader, gbc(0,0,1,1,1,0, new Insets(0,0,8,0)));
        left.add(searchField, gbc(0,1,1,1,1,0, new Insets(0,0,8,0)));
        left.add(listScroll,  gbc(0,2,1,1,1,1, new Insets(0,0,0,0)));

        // ===== RIGHT =====
        editor = new EditAccounts();
        editor.setOnSave((name, email, password, role) -> {
            User u = customerList.getSelectedValue();
            if (u == null) return;
            u.name = name.trim();
            u.email = email.trim();
            u.role = role.trim().isEmpty() ? u.role : role.trim();
            if (password != null && !password.isEmpty()) {
                u.password = password; // บันทึกตรงๆ ไม่ hash
            }
            customerList.repaint();
            saveUsersToCSV(CSV_PATH);
        });
        editor.setOnDelete(() -> {
            int idx = customerList.getSelectedIndex();
            if (idx >= 0) {
                users.remove(idx);
                listModel.remove(idx);
                saveUsersToCSV(CSV_PATH);
            }
        });

        grid.add(left,  gcLeft);
        grid.add(editor, gcRight);

        // ===== Load CSV =====
        loadUsersFromCSV(CSV_PATH);
        if (!users.isEmpty()) customerList.setSelectedIndex(0);
    }

    // ===== CSV =====
    private void loadUsersFromCSV(String file) {
        users.clear();
        listModel.clear();

        Path p = Paths.get(file);
        if (!Files.exists(p)) return;

        try (BufferedReader br = Files.newBufferedReader(p, StandardCharsets.UTF_8)) {
            String line; boolean header = true;
            while ((line = br.readLine()) != null) {
                if (header) { header = false; continue; }
                String[] a = line.split(",", -1);
                if (a.length >= 4) {
                    users.add(new User(a[0].trim(), a[1].trim(), a[2].trim(), a[3].trim()));
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        for (User u : users) listModel.addElement(u);
    }

    private void saveUsersToCSV(String file) {
        Path p = Paths.get(file);
        try (BufferedWriter bw = Files.newBufferedWriter(p, StandardCharsets.UTF_8)) {
            bw.write("Name,Email,Role,Password");
            bw.newLine();
            for (User u : users) {
                bw.write(u.name + "," + u.email + "," + u.role + "," + u.password);
                bw.newLine();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void filterList(String q) {
        listModel.clear();
        if (q.isEmpty()) {
            for (User u : users) listModel.addElement(u);
        } else {
            for (User u : users) {
                if (u.name.toLowerCase().contains(q) || u.email.toLowerCase().contains(q))
                    listModel.addElement(u);
            }
        }
    }

    // ===== Utils =====
    private GridBagConstraints gbc(int x,int y,int w,int h,double wx,double wy,Insets in){
        GridBagConstraints g = new GridBagConstraints();
        g.gridx=x; g.gridy=y; g.gridwidth=w; g.gridheight=h;
        g.weightx=wx; g.weighty=wy; g.insets=in;
        g.fill=GridBagConstraints.BOTH;
        return g;
    }
    private JLabel title(String text){
        JLabel lb = new JLabel(text);
        lb.setForeground(new Color(250, 220, 200));
        lb.setFont(lb.getFont().deriveFont(Font.BOLD, 14f));
        return lb;
    }
    private JTextField roundedField(){
        JTextField tf = new JTextField();
        tf.setBorder(new CompoundBorder(
                new MatteBorder(1,1,1,1, new Color(200,200,200)),
                new EmptyBorder(8,12,8,12)
        ));
        tf.setBackground(Color.WHITE);
        tf.setForeground(Color.DARK_GRAY);
        return tf;
    }
    private JButton pillOrange(String text){
        JButton b = new JButton(text){
            @Override protected void paintComponent(Graphics g){
                Graphics2D g2=(Graphics2D)g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(ORANGE);
                g2.fillRoundRect(0,0,getWidth(),getHeight(),20,20);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        b.setFocusPainted(false);
        b.setContentAreaFilled(false);
        b.setForeground(Color.WHITE);
        b.setBorder(new EmptyBorder(6,12,6,12));
        return b;
    }

    // ===== User model =====
    static class User {
        String name, email, role, password;
        User(String n, String e, String r, String p){ name=n; email=e; role=r; password=p; }
        @Override public String toString(){ return name; }
    }

    // ===== helper for search =====
    static class SimpleDoc implements javax.swing.event.DocumentListener {
        private final Runnable r;
        private SimpleDoc(Runnable r){ this.r = r; }
        public static SimpleDoc onChange(Runnable r){ return new SimpleDoc(r); }
        @Override public void insertUpdate(javax.swing.event.DocumentEvent e){ r.run(); }
        @Override public void removeUpdate(javax.swing.event.DocumentEvent e){ r.run(); }
        @Override public void changedUpdate(javax.swing.event.DocumentEvent e){ r.run(); }
    }
}
