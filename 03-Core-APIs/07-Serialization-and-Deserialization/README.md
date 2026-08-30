# Serialization & Deserialization

**Serialization** converts an object's state into a byte stream (for saving to disk, sending over a network, etc.). **Deserialization** reconstructs the object from that byte stream.

## Making a class serializable

```java
import java.io.Serializable;

class User implements Serializable {
    private static final long serialVersionUID = 1L;
    String name;
    int age;
}
```

- `Serializable` is a **marker interface** — it has no methods; it just signals to the JVM "instances of this class may be serialized."
- `serialVersionUID` is a version identifier for the class. If it's not declared explicitly, the JVM computes one automatically from the class's structure — which is fragile (any change to the class can change the computed value). Explicitly declaring it and bumping it deliberately gives control over compatibility between class versions.

## Writing (serializing) an object

```java
try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("user.ser"))) {
    oos.writeObject(new User("Alice", 30));
} catch (IOException e) {
    e.printStackTrace();
}
```

## Reading (deserializing) an object

```java
try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("user.ser"))) {
    User u = (User) ois.readObject();   // requires an explicit cast
} catch (IOException | ClassNotFoundException e) {
    e.printStackTrace();
}
```

- `readObject()` returns `Object` — always requires an explicit cast to the expected type.
- Reading declares `ClassNotFoundException` in addition to `IOException`, since the class of the serialized object must be resolvable/loadable on the reading side.

## `transient` fields

```java
class User implements Serializable {
    String name;
    transient String password;   // excluded from serialization — becomes null on deserialize
}
```

- Fields marked `transient` are **skipped** during serialization (e.g. for sensitive data like passwords, or fields that can't/shouldn't be serialized like a `Thread` or `Socket`) — they come back as the type's default value (`null`, `0`, `false`, ...) after deserialization.

## Practice Questions / Exercises

- Make a `User` class `Serializable` with a declared `serialVersionUID`, write an instance to a file, then read it back and print its fields.
- Add a `transient` field (e.g. `password`) — serialize and deserialize, and show that field comes back as `null`.
- Try making a class `Serializable` where one of its fields is a *non*-serializable type, and observe the runtime `NotSerializableException` when you attempt to serialize it.
- Serialize a `List<User>` (a list of multiple objects) to one file, then deserialize the whole list back.

## Interview Questions

**Q: What is the purpose of the `Serializable` interface, given that it has no methods to implement?**
A: It's a marker interface — its sole purpose is to flag a class as eligible for the JVM's built-in serialization mechanism. `ObjectOutputStream.writeObject()` checks for this marker at runtime and throws `NotSerializableException` if the object's class (or any non-transient field's class) doesn't implement it.

**Q: What is `serialVersionUID`, and what happens if you don't declare it?**
A: It's a version identifier used during deserialization to verify the sender's and receiver's class definitions are compatible. If not explicitly declared, the JVM computes one automatically based on the class's structure (fields, methods, etc.) — which means even a minor, compatible code change can alter the computed value and break deserialization of previously-serialized data with an `InvalidClassException`. Declaring it explicitly avoids that fragility.

**Q: What does the `transient` keyword do, and give an example of when you'd use it.**
A: It excludes a field from the serialization process entirely — the field is skipped when writing and comes back as its type's default value (`null`/`0`/`false`) after deserialization. Common uses: sensitive data that shouldn't be persisted/transmitted (passwords, tokens), or fields referencing genuinely non-serializable runtime resources (open `Socket`, `Thread`, file handles) that can't meaningfully survive serialization anyway.

**Q: What happens if you try to serialize an object whose class implements `Serializable`, but one of its non-transient fields is a type that doesn't?**
A: A runtime `NotSerializableException` is thrown at the point `writeObject()` reaches that field — implementing `Serializable` is not automatically inherited or "deep" for referenced objects; every non-transient, non-static field's runtime type must itself be serializable (or `null`) too.

**Q: Are `static` fields included in serialization?**
A: No — serialization only captures instance state. `static` fields belong to the class, not any particular instance, so they're never written out or restored by the default serialization mechanism; after deserialization, a static field simply has whatever value the currently-loaded class has (unrelated to what it was when the object was serialized).

**Q: What are some drawbacks of Java's built-in serialization mechanism that lead many real-world systems to prefer alternatives (JSON, Protocol Buffers, etc.)?**
A: It's JVM/Java-specific (not interoperable with non-Java systems), the binary format is verbose and not human-readable, it's version-fragile without careful `serialVersionUID` management, and deserializing untrusted data is a well-known security risk (a `readObject()` on attacker-controlled bytes can trigger arbitrary code execution via "gadget chains"). JSON/Protobuf/Avro are typically preferred for cross-language, more controlled, more secure data exchange.
