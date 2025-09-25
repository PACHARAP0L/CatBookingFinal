package Core.Domain;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;

public class AuthStore {

    // บันทึกไฟล์ในโฟลเดอร์ ./trie/users.txt
    private static final Path BASE = Paths.get(System.getProperty("user.dir"));
    private static final Path DIR  = BASE.resolve("Data");
    private static final Path FILE = DIR.resolve("User.csv");

    // บันทึกผู้ใช้ใหม่
    public static void saveUser(String username, String email, String phone, String passwordPlain) throws IOException {
        ensureFile();
        if (userExists(username)) throw new IOException("Username already exists.");

        // เก็บรหัสตรง ๆ ไม่แฮช
        String line = String.join(",", escape(username), escape(email),"user", escape(passwordPlain), escape(phone));

        try (BufferedWriter w = Files.newBufferedWriter(FILE, StandardCharsets.UTF_8, StandardOpenOption.APPEND)) {
            w.write(line);
            w.newLine();
        }
    }

    // ตรวจว่ามี user ซ้ำหรือไม่
    public static boolean userExists(String username) throws IOException {
        ensureFile();
        try (BufferedReader r = Files.newBufferedReader(FILE, StandardCharsets.UTF_8)) {
            String s;
            while ((s = r.readLine()) != null) {
                String[] parts = s.split("\\,", -1);
                if (parts.length >= 1 && unescape(parts[0]).equalsIgnoreCase(username)) {
                    return true;
                }
            }
        }
        return false;
    }

    // ตรวจล็อกอิน
    public static boolean checkLogin(String username, String passwordPlain) throws IOException {
        ensureFile();
        try (BufferedReader r = Files.newBufferedReader(FILE, StandardCharsets.UTF_8)) {
            String s;
            while ((s = r.readLine()) != null) {
                String[] p = s.split("\\,", -1);
                if (p.length >= 4 && unescape(p[0]).equalsIgnoreCase(username)) {
                    String savedPass = unescape(p[3]); // เก็บ plain text
                    return savedPass.equals(passwordPlain);
                }
            }
        }
        return false;
    }

    // ---------- helpers ----------
    private static void ensureFile() throws IOException {
        if (!Files.exists(DIR))  Files.createDirectories(DIR);
        if (!Files.exists(FILE)) Files.createFile(FILE);
    }

    private static String escape(String s)   { return s.replace(",", "%7C"); }
    private static String unescape(String s) { return s.replace("%7C", ","); }
}
