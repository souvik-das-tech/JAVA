# File-Based Data Storage Mini-Project

Extends [[01-Console-based-CRUD-App]] with **persistence** — data survives across program restarts, stored in a plain text (or CSV) file instead of only in memory. Ties together [[../03-Core-APIs/06-File-IO]] and [[../03-Core-APIs/05-try-with-resources]].

## What to build

Take the CRUD app's repository and back it with a file instead of (or in addition to, for in-memory speed) a `List`:

- **On startup**: read the data file (if it exists) line-by-line, parsing each line back into a model object, and load them into an in-memory `List` for fast access during the session.
- **On every mutation** (add/update/delete): rewrite the entire file from the current in-memory state (simplest correct approach for a small dataset), or append-only for adds with a more involved scheme for update/delete.
- **A simple line format**, e.g. CSV: `id,name,phone,email` — one record per line.

```java
// Save all contacts back to file after any mutation
try (BufferedWriter bw = new BufferedWriter(new FileWriter("contacts.txt"))) {
    for (Contact c : contacts) {
        bw.write(c.getId() + "," + c.getName() + "," + c.getPhone());
        bw.newLine();
    }
}

// Load contacts from file on startup
try (BufferedReader br = new BufferedReader(new FileReader("contacts.txt"))) {
    String line;
    while ((line = br.readLine()) != null) {
        String[] parts = line.split(",");
        contacts.add(new Contact(Integer.parseInt(parts[0]), parts[1], parts[2]));
    }
} catch (FileNotFoundException e) {
    // no file yet — start with an empty list, not an error
}
```

## What this project should exercise

- Reading/writing text files with `BufferedReader`/`BufferedWriter` in `try-with-resources`.
- Handling the "file doesn't exist yet" case (first run) gracefully rather than crashing.
- A simple serialization format (CSV) and its inverse parsing, including handling a field that might itself contain the delimiter character (a real edge case worth at least thinking through, even if you don't fully solve escaping for this mini-project).
- Deciding *when* to persist — after every single mutation (simpler, safer against a mid-session crash) vs. only on exit (fewer writes, but risks losing changes if the program crashes/is killed).

## Practice Questions / Exercises

- Extend the Console CRUD app to load its data from a file on startup and save it back after every mutation.
- Handle the first-run case where the data file doesn't exist yet.
- Add a field containing a comma to a record's data and observe the CSV parsing breaking — discuss (in the README or in comments) how you'd fix it (quoting, or a different delimiter).
- Compare "rewrite the whole file on every mutation" vs. "save only on exit" — write down the trade-off for each in a sentence.

## Interview Questions

**Q: Why should file reading on startup handle a missing file gracefully instead of treating it as an error?**
A: The very first time the program runs, no data file exists yet — that's an entirely normal, expected condition, not a failure. Catching `FileNotFoundException` (or checking `File.exists()` first) and simply starting with an empty in-memory collection in that case correctly distinguishes "first run" from a genuine I/O problem.

**Q: What's the trade-off between rewriting the entire file after every mutation vs. saving only when the program exits?**
A: Rewriting after every mutation is simpler to reason about and safe against data loss if the program crashes or is killed mid-session (each committed change is durable immediately), at the cost of more disk I/O per operation. Saving only on exit is more efficient (one write at the end) but risks losing all changes made during the session if the program terminates abnormally (crash, `kill -9`, power loss) before that final save happens.

**Q: Why is a naive CSV format (splitting on `,`) fragile, and how would you fix it?**
A: If any field's actual data contains the delimiter character (e.g. a name like "Smith, Jr."), naive splitting misparses the line into the wrong number of fields, corrupting the record. Real fixes include quoting fields that contain the delimiter (the standard CSV approach, e.g. `"Smith, Jr.",...`) with a proper parser that understands quoting, or choosing a delimiter unlikely to appear in the data (e.g. a tab or a rare character), or switching to a format with built-in escaping (JSON).

**Q: Why load the whole file into an in-memory `List` on startup instead of reading from the file directly on every operation?**
A: Reading from disk repeatedly for every single lookup/list operation is far slower than reading once and working against an in-memory structure — file I/O is orders of magnitude slower than memory access. Loading once at startup (and writing back only on mutation) gives fast reads during the session while still persisting changes.

**Q: How does this project's approach differ fundamentally from what a real database offers?**
A: A real database provides transactional guarantees (atomicity — a crash mid-write doesn't leave a half-written, corrupted file), concurrent access control (multiple processes/threads safely reading/writing at once), indexing for efficient lookups without scanning the whole dataset, and a query language — none of which "rewrite a text file" gives you. This mini-project's manual approach is a useful learning exercise but not something you'd use for anything beyond a toy/personal tool; see [[04-JDBC-based-Project]] for the next step.
