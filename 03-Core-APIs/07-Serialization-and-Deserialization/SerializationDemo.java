// Q: Make a User class (name, age, and a transient password field)
// implement Serializable with an explicit serialVersionUID. In main, create
// a User, serialize it to user.ser via ObjectOutputStream, then deserialize
// it back via ObjectInputStream and print all three fields — confirming
// password comes back as null since it's transient.

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

class User implements Serializable {
    private static final long serialVersionUID = 1L;
    String name;
    int age;
    transient String password;

    User(String name, int age, String password) {
        this.name = name;
        this.age = age;
        this.password = password;
    }
}

public class SerializationDemo {
    public static void main(String[] args) {
        File file = new File("user.ser");
        User original = new User("Alice", 30, "secret123");

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(original);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            User restored = (User) ois.readObject();
            System.out.println("name = " + restored.name);
            System.out.println("age = " + restored.age);
            System.out.println("password = " + restored.password);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        file.delete();
    }
}
