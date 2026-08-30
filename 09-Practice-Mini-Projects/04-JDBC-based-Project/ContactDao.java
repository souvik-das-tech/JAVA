// Q: Write a ContactDao class with the same method shape as the file-based
// ContactRepository (add, findAll, update, delete), backed by a `contacts`
// table (id, name, phone) via JDBC. Use RETURN_GENERATED_KEYS to get the new
// row's id back on add(). Use PreparedStatement everywhere.
//
// NOTE: requires a real database server and its JDBC driver .jar on the
// classpath — it will not run in this sandbox (no DB, no driver jar
// available here). Adapt the connection URL/driver once you have MySQL or
// Postgres running locally, and create the `contacts` table first:
//   CREATE TABLE contacts (
//       id INT AUTO_INCREMENT PRIMARY KEY,
//       name VARCHAR(100) NOT NULL,
//       phone VARCHAR(20)
//   );

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

class Contact {
    private final int id;
    private final String name;
    private final String phone;

    Contact(int id, String name, String phone) {
        this.id = id;
        this.name = name;
        this.phone = phone;
    }

    @Override
    public String toString() {
        return id + ": " + name + " (" + phone + ")";
    }
}

class ContactDao {
    private final String url;
    private final String user;
    private final String password;

    ContactDao(String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;
    }

    Contact add(String name, String phone) throws SQLException {
        String sql = "INSERT INTO contacts (name, phone) VALUES (?, ?)";
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, name);
            ps.setString(2, phone);
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                keys.next();
                return new Contact(keys.getInt(1), name, phone);
            }
        }
    }

    List<Contact> findAll() throws SQLException {
        List<Contact> result = new ArrayList<>();
        String sql = "SELECT id, name, phone FROM contacts";
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                result.add(new Contact(rs.getInt("id"), rs.getString("name"), rs.getString("phone")));
            }
        }
        return result;
    }

    boolean update(int id, String newPhone) throws SQLException {
        String sql = "UPDATE contacts SET phone = ? WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newPhone);
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        }
    }

    boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM contacts WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    public static void main(String[] args) throws SQLException {
        ContactDao dao = new ContactDao("jdbc:mysql://localhost:3306/mydb", "root", "password");
        dao.add("Alice", "111-2222");
        dao.findAll().forEach(System.out::println);
        dao.update(1, "999-0000");
        dao.delete(1);
    }
}
