// Q: Using JDBC, connect to a local MySQL/Postgres database (adjust the URL,
// username, password, and driver for your setup) and implement CRUD against
// a `users` table (id, name, age): insertUser(name, age), getAllUsers(),
// updateUserAge(name, newAge), deleteUser(name). Use PreparedStatement for
// every query, and try-with-resources for Connection/PreparedStatement/
// ResultSet. Call all four from main against a running database.
//
// NOTE: this requires a real database server and its JDBC driver .jar on
// the classpath — it will not run in this sandbox (no DB, no driver jar
// available here). Adapt the connection URL/driver below once you have
// MySQL or Postgres running locally.

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JdbcCrudDemo {
    static final String URL = "jdbc:mysql://localhost:3306/mydb";
    static final String USER = "root";
    static final String PASSWORD = "password";

    static void insertUser(String name, int age) throws SQLException {
        String sql = "INSERT INTO users (name, age) VALUES (?, ?)";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setInt(2, age);
            ps.executeUpdate();
        }
    }

    static void getAllUsers() throws SQLException {
        String sql = "SELECT id, name, age FROM users";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                System.out.println(rs.getInt("id") + " " + rs.getString("name") + " " + rs.getInt("age"));
            }
        }
    }

    static void updateUserAge(String name, int newAge) throws SQLException {
        String sql = "UPDATE users SET age = ? WHERE name = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, newAge);
            ps.setString(2, name);
            ps.executeUpdate();
        }
    }

    static void deleteUser(String name) throws SQLException {
        String sql = "DELETE FROM users WHERE name = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.executeUpdate();
        }
    }

    public static void main(String[] args) throws SQLException {
        insertUser("Alice", 30);
        getAllUsers();
        updateUserAge("Alice", 31);
        deleteUser("Alice");
    }
}
