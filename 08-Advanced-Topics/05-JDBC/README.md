# JDBC — Connecting to a Database, CRUD Operations

JDBC (Java Database Connectivity) is the standard API (`java.sql`, built into the JDK) for connecting to and executing SQL against a relational database — MySQL, PostgreSQL, etc. — through a database-specific **driver**.

> Note: the demo `.java` file in this folder compiles cleanly (`java.sql` is part of the JDK), but actually **running** it requires a real database server and its JDBC driver `.jar` on the classpath (e.g. `mysql-connector-j` for MySQL, `postgresql` for Postgres) — neither is set up in this environment, so it can't be executed here. Treat the code as a reference to adapt once you have a real MySQL/Postgres instance running (this maps directly to the [[../09-Practice-Mini-Projects/04-JDBC-based-Project]] mini-project).

## Connecting

```java
String url = "jdbc:mysql://localhost:3306/mydb";
try (Connection conn = DriverManager.getConnection(url, "username", "password")) {
    // use conn...
} catch (SQLException e) {
    e.printStackTrace();
}
```

- Since JDBC 4.0 (Java 6+), driver registration is automatic (via `META-INF/services`) as long as the driver `.jar` is on the classpath — no more manual `Class.forName("com.mysql.cj.jdbc.Driver")`.
- `Connection` implements `AutoCloseable` — always use `try-with-resources` (see [[../03-Core-APIs/05-try-with-resources]]).

## `Statement` vs `PreparedStatement`

```java
// Statement — vulnerable to SQL injection if building SQL via string concatenation
Statement stmt = conn.createStatement();
stmt.executeQuery("SELECT * FROM users WHERE name = '" + userInput + "'");   // DON'T DO THIS

// PreparedStatement — parameterized, safe, and pre-compiled (faster if reused)
PreparedStatement ps = conn.prepareStatement("SELECT * FROM users WHERE name = ?");
ps.setString(1, userInput);
ResultSet rs = ps.executeQuery();
```

- **Always prefer `PreparedStatement`** for any query involving external input — it prevents SQL injection by treating parameters as data, never as executable SQL syntax, and the DB can cache the compiled query plan across executions.

## CRUD via JDBC

```java
// CREATE
PreparedStatement insert = conn.prepareStatement("INSERT INTO users (name, age) VALUES (?, ?)");
insert.setString(1, "Alice");
insert.setInt(2, 30);
insert.executeUpdate();               // returns rows affected — use executeUpdate() for INSERT/UPDATE/DELETE

// READ
PreparedStatement select = conn.prepareStatement("SELECT id, name, age FROM users");
ResultSet rs = select.executeQuery(); // use executeQuery() for SELECT
while (rs.next()) {
    System.out.println(rs.getInt("id") + " " + rs.getString("name") + " " + rs.getInt("age"));
}

// UPDATE
PreparedStatement update = conn.prepareStatement("UPDATE users SET age = ? WHERE name = ?");
update.setInt(1, 31);
update.setString(2, "Alice");
update.executeUpdate();

// DELETE
PreparedStatement delete = conn.prepareStatement("DELETE FROM users WHERE name = ?");
delete.setString(1, "Alice");
delete.executeUpdate();
```

- `executeQuery()` — for `SELECT`, returns a `ResultSet`.
- `executeUpdate()` — for `INSERT`/`UPDATE`/`DELETE`/DDL, returns the number of rows affected (an `int`), not a `ResultSet`.
- `ResultSet.next()` — advances to the next row, returns `false` when there are no more; the cursor starts *before* the first row, so `next()` must be called once before the first `get...()` call.

## Practice Questions / Exercises

- Set up a local MySQL or PostgreSQL instance (or use Docker), download the appropriate JDBC driver `.jar`, and adapt this topic's demo to actually connect and run.
- Write `PreparedStatement`-based `insertUser`, `getAllUsers`, `updateUserAge`, and `deleteUser` methods against a `users` table.
- Compare (in a comment or discussion) what would happen with string-concatenated SQL if `userInput` were `' OR '1'='1`, versus a `PreparedStatement` with the same input.
- Use `try-with-resources` for `Connection`, `PreparedStatement`, and `ResultSet` together, and explain the closing order.

## Interview Questions

**Q: What is JDBC, and what role does the "driver" play?**
A: JDBC is Java's standard API for connecting to and interacting with relational databases via SQL. The driver is a database-vendor-specific implementation (a `.jar` on the classpath, e.g. MySQL Connector/J) that translates JDBC's generic API calls into that specific database's actual wire protocol — JDBC code itself is largely database-agnostic, only the connection URL and driver differ per database.

**Q: Why is `PreparedStatement` preferred over `Statement` for queries involving user input?**
A: `Statement` executes raw SQL strings, so building a query via string concatenation with untrusted input opens the door to SQL injection (an attacker crafting input that alters the query's logic). `PreparedStatement` sends the SQL template and parameter values separately — the database treats parameters strictly as data, never as executable SQL — eliminating injection risk, and as a bonus the query plan can be compiled once and reused across executions.

**Q: What's the difference between `executeQuery()` and `executeUpdate()`?**
A: `executeQuery()` is for `SELECT` statements and returns a `ResultSet` containing the retrieved rows. `executeUpdate()` is for `INSERT`/`UPDATE`/`DELETE` (and DDL statements) and returns an `int` — the number of rows affected — since there's no result set to return for those operations.

**Q: Why must `ResultSet.next()` be called before reading the first row?**
A: A `ResultSet`'s cursor starts positioned *before* the first row, not on it — `next()` both advances the cursor and returns whether a row is now available. Calling a `get...()` method before any `next()` call (or after `next()` returns `false`) throws a `SQLException`, since the cursor isn't positioned on a valid row.

**Q: Why should `Connection`, `Statement`, and `ResultSet` all be used with `try-with-resources`?**
A: All three represent limited external/native resources (a database connection socket, server-side cursor state) that must be explicitly released — leaving them open leaks connections and can exhaust the database's connection pool. `try-with-resources` guarantees they're closed automatically, in reverse declaration order, even if an exception occurs mid-operation.

**Q: What is connection pooling, and why is a new `Connection` per request generally avoided in real applications?**
A: Establishing a raw database connection (TCP handshake, authentication) is relatively expensive; connection pooling (e.g. HikariCP) maintains a reusable pool of already-open connections that application code borrows and returns rather than opening/closing a fresh one per request. Real (especially high-throughput) applications almost always use a pool rather than `DriverManager.getConnection()` directly per request, for both performance and to avoid exhausting the database's max-connections limit.
