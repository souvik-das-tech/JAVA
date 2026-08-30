# JDBC-Based Project (Connect to MySQL/Postgres)

The natural extension of [[02-File-based-Data-Storage-Mini-Project]] — swap the hand-rolled CSV file for a real relational database, using the CRUD patterns from [[../08-Advanced-Topics/05-JDBC]].

> Note: same limitation as [[../08-Advanced-Topics/05-JDBC]] — the demo `.java` file here compiles cleanly against the JDK's built-in `java.sql`, but running it needs a real MySQL/Postgres server plus its driver `.jar`, neither available in this sandbox. Set up a local database (or Docker container) to actually run it.

## What to build

A `ContactDao` (Data Access Object) class wrapping JDBC CRUD against a `contacts` table, replacing the [[02-File-based-Data-Storage-Mini-Project]]'s file-based `ContactRepository` with the *same public method shape* (`add`, `findAll`, `update`, `delete`) — the console/menu layer shouldn't need to change at all, since it only depends on that interface, not on how the data is stored (this is the payoff of the layering discussed in [[01-Console-based-CRUD-App]]).

```sql
CREATE TABLE contacts (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    phone VARCHAR(20)
);
```

```java
class ContactDao {
    private final String url, user, password;

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
    // findAll, update, delete follow the same PreparedStatement pattern as ../../08-Advanced-Topics/05-JDBC
}
```

## What this project should exercise

- Retrieving an auto-generated primary key after an `INSERT` (`Statement.RETURN_GENERATED_KEYS` + `getGeneratedKeys()`), instead of managing ids manually in Java.
- Mapping `ResultSet` rows back into your model objects (a manual, hand-written version of what an ORM like Hibernate automates).
- Comparing this DAO's method signatures against the file-based repository's — noticing they can be made to match, which is the whole point of the DAO pattern.
- Handling `SQLException` appropriately at each layer — decide whether the DAO should let it propagate (checked, forcing the caller to handle it) or wrap it in an unchecked custom exception (see [[../03-Core-APIs/04-Custom-Exceptions]]).

## Practice Questions / Exercises

- Set up a local MySQL/Postgres instance (or Docker container), create the `contacts` table, add the JDBC driver to your classpath, and get the demo `.java` file in this folder actually connecting and running.
- Implement `findAll()` mapping every row of a `SELECT * FROM contacts` into a `List<Contact>`.
- Implement `add()` using `RETURN_GENERATED_KEYS` to get the new row's id back without a second query.
- Swap the Console CRUD app's repository field from the file-based one to this DAO, and confirm the menu/IO code needs zero changes.

## Interview Questions

**Q: How do you retrieve an auto-generated primary key after an `INSERT` in JDBC?**
A: Prepare the statement with `conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)`, execute the update, then call `ps.getGeneratedKeys()` to get a `ResultSet` containing the generated key(s) — call `.next()` on it and read the key column (typically via `getInt(1)`), rather than needing a separate query to look up the id you just inserted.

**Q: What's the benefit of a DAO (Data Access Object) layer having the same method shape as an earlier in-memory/file-based repository?**
A: It lets the rest of the application (the menu/business logic layer) depend only on an abstraction (`add`, `findAll`, `update`, `delete`) rather than on *how* data is stored — swapping the backing implementation (in-memory → file → real database) requires touching only the DAO/repository implementation, not any code that calls it. This is the Dependency Inversion idea in practice, even without formally declaring a Java `interface` for it (though doing so would make the contract explicit).

**Q: Should a DAO method declare `throws SQLException`, or should it catch and wrap it in a custom exception?**
A: Either can be reasonable depending on the layering: declaring `throws SQLException` (a checked exception) forces every caller to explicitly handle or propagate database failures, which can be appropriate for a small app. In a larger application, DAOs often catch `SQLException` and wrap it in an unchecked custom exception (e.g. `DataAccessException`) so callers aren't forced to handle low-level JDBC exception types throughout unrelated business logic — see [[../03-Core-APIs/04-Custom-Exceptions]] for the checked-vs-unchecked design trade-off.

**Q: What manual work does a hand-written JDBC DAO do that an ORM (like Hibernate/JPA) automates?**
A: Manually mapping each `ResultSet` row's columns into a Java object's fields (and the reverse, mapping object fields into `PreparedStatement` parameters for inserts/updates), managing SQL strings directly, and hand-writing the CRUD boilerplate for every entity. An ORM automates this mapping via annotations/configuration and generates the SQL itself, at the cost of an additional abstraction layer and its own learning curve/potential performance opacity.

**Q: Why would the `contacts` table's `id` column be defined `AUTO_INCREMENT` (MySQL) / a `SERIAL`/identity column (Postgres) rather than having the Java code assign ids, as the file-based version did?**
A: A database can guarantee id uniqueness and correct concurrent assignment natively (even under many simultaneous inserts from different connections/threads/application instances) far more reliably than application-level counter logic, which would need careful synchronization to avoid duplicate ids under concurrent access — see [[../06-Multithreading/02-Synchronization]] for why a naively shared counter isn't safe across threads, let alone across multiple application instances hitting the same database.
