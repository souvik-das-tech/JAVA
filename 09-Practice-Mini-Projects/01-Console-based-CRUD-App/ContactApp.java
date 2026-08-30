// Q: Build a console-based CRUD app for Contact (id, name, phone) backed by
// an in-memory List, with a menu loop (Add/View All/Update/Delete/Exit)
// driven by Scanner. Handle invalid numeric input without crashing, and
// handle update/delete on a non-existent id gracefully. Since this needs
// interactive stdin, main() below runs a scripted demo sequence instead of
// reading System.in, exercising the same ContactRepository logic the real
// menu loop would drive.

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

class Contact {
    private final int id;
    private String name;
    private String phone;

    Contact(int id, String name, String phone) {
        this.id = id;
        this.name = name;
        this.phone = phone;
    }

    int getId() {
        return id;
    }

    String getName() {
        return name;
    }

    void setName(String name) {
        this.name = name;
    }

    String getPhone() {
        return phone;
    }

    void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return id + ": " + name + " (" + phone + ")";
    }
}

class ContactRepository {
    private final List<Contact> contacts = new ArrayList<>();
    private int nextId = 1;

    Contact add(String name, String phone) {
        Contact c = new Contact(nextId++, name, phone);
        contacts.add(c);
        return c;
    }

    List<Contact> findAll() {
        return contacts;
    }

    boolean update(int id, String newPhone) {
        Optional<Contact> found = contacts.stream().filter(c -> c.getId() == id).findFirst();
        found.ifPresent(c -> c.setPhone(newPhone));
        return found.isPresent();
    }

    boolean delete(int id) {
        return contacts.removeIf(c -> c.getId() == id);
    }
}

public class ContactApp {
    public static void main(String[] args) {
        ContactRepository repo = new ContactRepository();

        repo.add("Alice", "111-2222");
        repo.add("Bob", "333-4444");
        System.out.println("After adding two contacts:");
        repo.findAll().forEach(System.out::println);

        boolean updated = repo.update(1, "999-0000");
        System.out.println("Update id 1 succeeded? " + updated);
        System.out.println("After update:");
        repo.findAll().forEach(System.out::println);

        boolean updatedMissing = repo.update(99, "000-0000");
        System.out.println("Update non-existent id 99 succeeded? " + updatedMissing);

        boolean deleted = repo.delete(2);
        System.out.println("Delete id 2 succeeded? " + deleted);
        System.out.println("After delete:");
        repo.findAll().forEach(System.out::println);

        boolean deletedMissing = repo.delete(42);
        System.out.println("Delete non-existent id 42 succeeded? " + deletedMissing);
    }
}
