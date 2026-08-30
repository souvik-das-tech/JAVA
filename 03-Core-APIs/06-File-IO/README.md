# File I/O

Java's classic (`java.io`) file APIs — still widely used, especially `BufferedReader`/`BufferedWriter` for text.

## `File`

Represents a path on the filesystem (doesn't itself open/read anything) — used for metadata and existence checks.

```java
File f = new File("data.txt");
f.exists(); f.isDirectory(); f.length(); f.delete(); f.mkdir();
```

## Reading text — `FileReader` + `BufferedReader`

```java
try (BufferedReader br = new BufferedReader(new FileReader("data.txt"))) {
    String line;
    while ((line = br.readLine()) != null) {
        System.out.println(line);
    }
} catch (IOException e) {
    e.printStackTrace();
}
```

- `FileReader` reads raw characters one at a time from disk — slow if used directly. `BufferedReader` wraps it, buffering and adding `readLine()` for efficient line-by-line reading.

## Writing text — `FileWriter` + `BufferedWriter`

```java
try (BufferedWriter bw = new BufferedWriter(new FileWriter("out.txt"))) {
    bw.write("Hello, file!");
    bw.newLine();
    bw.write("Second line");
} catch (IOException e) {
    e.printStackTrace();
}
```

- `new FileWriter("out.txt")` **overwrites** the file by default; `new FileWriter("out.txt", true)` appends instead.
- `BufferedWriter` buffers writes in memory and flushes them together — far fewer, larger disk writes than writing character-by-character.

## Practice Questions / Exercises

- Use `File` to check if a file exists, and create it if not (`createNewFile()`); print its absolute path and size.
- Write several lines to a file using `BufferedWriter`, then read them back line-by-line with `BufferedReader` and print each.
- Append to an existing file (using the two-argument `FileWriter` constructor) without erasing its previous content.
- Count the number of lines in a text file using `BufferedReader.readLine()` in a loop.

## Interview Questions

**Q: What's the difference between `File` and `FileReader`/`FileWriter`?**
A: `File` represents a path/filesystem entry and its metadata (exists, is a directory, size, etc.) — it doesn't read or write file *contents*. `FileReader`/`FileWriter` are actual I/O streams that read/write a file's character content.

**Q: Why wrap `FileReader` in a `BufferedReader` instead of reading directly?**
A: `FileReader` reads character-by-character directly from the underlying OS file handle, which is slow due to per-character system call overhead. `BufferedReader` adds an in-memory buffer, reading larger chunks at once and serving individual reads (and convenient `readLine()`) from that buffer — drastically fewer actual disk I/O operations.

**Q: What happens if you open a file with `new FileWriter("out.txt")` when the file already has content?**
A: It truncates the file first — all existing content is lost, and writing starts from an empty file. To append instead of overwrite, use the two-argument constructor `new FileWriter("out.txt", true)`.

**Q: Why is it recommended to use `try-with-resources` with file streams?**
A: File handles are a limited OS resource — failing to close them (e.g. because an exception was thrown mid-read/write) can leak file descriptors, leave data unflushed in a buffered writer, or keep a file locked. `try-with-resources` guarantees `close()` runs even on an exception.

**Q: What's the difference between `IOException` being checked vs how a typical bug like `NullPointerException` is handled?**
A: `IOException` is a checked exception — the compiler forces you to either catch it or declare `throws IOException`, since I/O failures (missing file, permission denied, disk full) are expected, environment-dependent conditions a caller should plan for. `NullPointerException` is unchecked, since it represents a programming bug, not an expected operating condition.

**Q: What does `BufferedWriter.flush()` do, and when is it necessary if you're already using `try-with-resources`?**
A: `flush()` forces any buffered-but-not-yet-written data out to the underlying stream immediately, rather than waiting for the buffer to fill or the stream to close. With `try-with-resources`, `close()` (called automatically) also flushes remaining data, so an explicit `flush()` before that point is only needed if you need the data to reach disk *before* the block ends (e.g. another process needs to read it mid-way).
