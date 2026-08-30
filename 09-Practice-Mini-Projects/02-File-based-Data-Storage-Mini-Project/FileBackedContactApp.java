// Q: Extend the Console CRUD app so ContactRepository loads its contacts
// from a CSV file on startup (id,name,phone per line) and rewrites the whole
// file after every mutation. Handle the file-not-found case on first run
// gracefully. In main: create a repository against a demo file, add a
// couple of contacts (persisting to disk), then create a SECOND repository
// instance against the same file to prove the data was actually reloaded
// from disk, not just held in memory.

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
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
    private final File file;
    private int nextId = 1;

    ContactRepository(String path) {
        this.file = new File(path);
        load();
    }

    private void load() {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                int id = Integer.parseInt(parts[0]);
                contacts.add(new Contact(id, parts[1], parts[2]));
                nextId = Math.max(nextId, id + 1);
            }
        } catch (FileNotFoundException e) {
            // first run — no file yet, start empty
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void save() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
            for (Contact c : contacts) {
                bw.write(c.getId() + "," + c.getName() + "," + c.getPhone());
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    Contact add(String name, String phone) {
        Contact c = new Contact(nextId++, name, phone);
        contacts.add(c);
        save();
        return c;
    }

    List<Contact> findAll() {
        return contacts;
    }

    boolean update(int id, String newPhone) {
        Optional<Contact> found = contacts.stream().filter(c -> c.getId() == id).findFirst();
        found.ifPresent(c -> c.setPhone(newPhone));
        if (found.isPresent()) save();
        return found.isPresent();
    }
}

public class FileBackedContactApp {
    public static void main(String[] args) {
        File dataFile = new File("contacts_demo.txt");
        dataFile.delete(); // ensure a clean first-run state for this demo

        ContactRepository repo1 = new ContactRepository(dataFile.getPath());
        repo1.add("Alice", "111-2222");
        repo1.add("Bob", "333-4444");
        System.out.println("repo1 contacts (in-memory, just added):");
        repo1.findAll().forEach(System.out::println);

        ContactRepository repo2 = new ContactRepository(dataFile.getPath());
        System.out.println("repo2 contacts (freshly loaded from disk):");
        repo2.findAll().forEach(System.out::println);

        repo2.update(1, "999-0000");
        System.out.println("repo2 after update:");
        repo2.findAll().forEach(System.out::println);

        dataFile.delete();
    }
}
