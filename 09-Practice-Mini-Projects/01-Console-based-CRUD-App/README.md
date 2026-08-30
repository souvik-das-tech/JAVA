# Console-Based CRUD App

A small end-to-end project tying together several earlier topics: [[../02-OOP/03-Encapsulation]], [[../04-Collections/02-List-ArrayList-LinkedList]], [[../03-Core-APIs/03-Exception-Handling]], and [[../06-Multithreading/../../03-Core-APIs/09-Input-Handling]]-style console input (via `Scanner`).

## What to build

An in-memory (no file/database persistence — that's the next two mini-projects) console application that manages a simple record type — e.g. `Contact` (name, phone, email) or `Task` (title, done) — via a text menu loop:

```
1. Add
2. View All
3. Update
4. Delete
5. Exit
Choose an option:
```

## Suggested structure

- A model class (e.g. `Contact`) with private fields, a constructor, getters, and a `toString()`.
- A "repository"-style class (e.g. `ContactRepository`) wrapping a `List<Contact>` (or `Map<Integer, Contact>` keyed by an auto-incrementing id), with `add`, `findAll`, `update`, `delete` methods — this is exactly the encapsulation pattern from [[../02-OOP/03-Encapsulation]] applied to a whole collection.
- A `main` loop reading menu choices via `Scanner`, validating input (handle non-numeric input with a `try/catch` around `Integer.parseInt`, per [[../03-Core-APIs/03-Exception-Handling]]), and dispatching to the repository.

## What this project should exercise

- Looping menu-driven input with `Scanner`, re-prompting on invalid input instead of crashing.
- CRUD logic against an in-memory `List`/`Map` — assigning and tracking a simple auto-incrementing id.
- Clean separation between the model, the "data layer," and the menu/IO loop — resist the urge to put everything in `main`.
- Handling the "not found" case gracefully (e.g. updating/deleting an id that doesn't exist) without an unhandled exception crashing the whole app.

## Practice Questions / Exercises

- Design the `Contact` (or your chosen entity) class and its repository class first, with a `README`-only plan, before writing any menu/IO code.
- Implement `Add` and `View All` first, test them thoroughly via the console, then add `Update` and `Delete`.
- Handle invalid menu input (e.g. typing "abc" instead of a number) without the program crashing.
- Handle `Update`/`Delete` on a non-existent id by printing a friendly message instead of throwing.

## Interview Questions

**Q: Why separate the "repository" (data access) logic from the menu/IO loop in a small console app like this?**
A: It mirrors real-world layering (even without a database yet) — the repository owns the collection and its invariants (e.g. unique ids), while the menu loop only handles user interaction and delegates actual data operations to it. This separation makes the repository reusable/testable independent of console I/O, and is exactly the shape you'd extend later by swapping the in-memory `List` for a real database-backed implementation (see [[04-JDBC-based-Project]]) without touching the menu code at all.

**Q: How would you generate unique ids for new records in an in-memory list without a database's auto-increment?**
A: A simple approach: keep a `static` or instance `int nextId` counter, incrementing it each time a record is added and assigning the pre-increment (or post-increment) value as that record's id — or, if using a `Map<Integer, Contact>`, compute `nextId` as `(map.keySet().stream().max(...).orElse(0)) + 1` each time, though a dedicated counter field is simpler and more efficient.

**Q: What's a robust way to handle invalid numeric menu input without crashing the program?**
A: Wrap `Integer.parseInt(scanner.nextLine())` in a `try/catch (NumberFormatException e)`, print a friendly re-prompt message, and loop back to ask again — never let an uncaught exception from bad input propagate up and terminate the whole menu loop, since a console app's very purpose is tolerating human typos gracefully.

**Q: Why is it a design smell to put all logic (model fields, storage, and console I/O) directly inside `main()`?**
A: It couples unrelated concerns together, making the code hard to test (you can't unit-test "add a contact" without also driving fake console input) and hard to extend (swapping in a real database later means untangling storage logic from the IO loop it's currently welded to). Separating model/repository/IO layers, even in a small app, keeps each piece independently reasonable and reusable.

**Q: How would this project change if you needed to support concurrent access from multiple "users" (threads) at once?**
A: The in-memory `List`/`Map` in the repository would need thread-safety — e.g. swapping to a `ConcurrentHashMap` (see [[../06-Multithreading/07-Concurrent-Collections]]) or wrapping mutating operations in `synchronized`, since concurrent `add`/`update`/`delete` calls from multiple threads against a plain `ArrayList`/`HashMap` risk race conditions or `ConcurrentModificationException`.
