# REST-ish Project Practice (Pre-Spring Warm-Up)

Before learning Spring Boot's `@RestController`/`@GetMapping` conveniences, it's worth seeing what a REST API actually *is* underneath — routing an incoming HTTP request (method + path) to some handler code that reads/writes data and returns a response. This project builds a tiny JSON-ish HTTP API using only the **JDK's built-in `com.sun.net.httpserver.HttpServer`** (no framework, no external dependencies) — everything Spring will later do for you automatically, done by hand once.

## What to build

An in-memory `contacts` HTTP API:

| Method | Path | Behavior |
|---|---|---|
| `GET` | `/contacts` | List all contacts |
| `POST` | `/contacts` | Create a contact from the request body |
| `DELETE` | `/contacts?id=1` | Delete a contact by id |

```java
HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
server.createContext("/contacts", exchange -> {
    String method = exchange.getRequestMethod();
    switch (method) {
        case "GET" -> handleList(exchange);
        case "POST" -> handleCreate(exchange);
        case "DELETE" -> handleDelete(exchange);
        default -> exchange.sendResponseHeaders(405, -1);   // Method Not Allowed
    }
});
server.start();
```

- `HttpExchange` gives you the request method, path/query, headers, and body input stream — and lets you write a status code + response body back.
- This is deliberately **not** production-grade (no real JSON library, minimal error handling, single-threaded by default) — the point is understanding the request/response cycle a framework like Spring later automates, not building something you'd ship.

## What this project should exercise

- Manually routing based on HTTP method + path — exactly what `@GetMapping`/`@PostMapping` do for you in Spring.
- Reading a request body (`exchange.getRequestBody()`, an `InputStream`) and parsing it — here, a tiny hand-rolled query-string-style format instead of real JSON, to avoid needing a JSON library.
- Setting response status codes correctly (`200` OK, `201` Created, `404` Not Found, `405` Method Not Allowed) and writing a response body via `exchange.getResponseBody()`.
- Testing the API with `curl` or a browser/Postman while it's running, seeing the request/response cycle end-to-end.

## Practice Questions / Exercises

- Run the server from this topic's demo file, then use `curl http://localhost:8080/contacts` to list contacts, `curl -X POST -d "name=Alice&phone=111-2222" http://localhost:8080/contacts` to create one, and `curl -X DELETE "http://localhost:8080/contacts?id=1"` to delete one.
- Add a `GET /contacts/{id}` route returning a single contact (or a `404` if not found) — note how much more path-parsing work this needs by hand vs. Spring's `@GetMapping("/contacts/{id}")`.
- Add basic validation: return `400 Bad Request` if `POST /contacts` is missing a `name`.
- After finishing this, skim what the same API would look like as a Spring Boot `@RestController` — notice how much of this file's boilerplate (server setup, method routing, manual status codes) Spring handles via annotations.

## Interview Questions

**Q: What does "REST" actually mean, at a basic level, and how does this project reflect it?**
A: REST (Representational State Transfer) is an architectural style where resources (here, `contacts`) are addressed by URLs, and standard HTTP methods express the operation on that resource — `GET` to read, `POST` to create, `PUT`/`PATCH` to update, `DELETE` to remove. This project reflects that directly: the same `/contacts` path behaves differently purely based on which HTTP method the request uses.

**Q: What is `HttpExchange`, and what are its two main responsibilities in a handler?**
A: It represents one in-flight HTTP request/response cycle. On the request side, it exposes the method, path/query string, headers, and request body (as an `InputStream`) to read. On the response side, your handler must call `sendResponseHeaders(statusCode, responseLength)` and then write to `getResponseBody()` (an `OutputStream`) — and must close the exchange (or the output stream) when done, or the client's connection hangs.

**Q: Why does this project avoid using a real JSON library?**
A: To keep the dependency-free, "just the JDK" spirit of the exercise — the focus is understanding raw HTTP routing/request/response handling, not JSON parsing. A real project would use a library (like Jackson, which Spring Boot includes by default) rather than hand-rolling ad hoc string parsing, which is fragile and only used here as a simplification.

**Q: What status code should `POST /contacts` return on success, and why not just `200`?**
A: `201 Created` is the more precise, RESTfully correct status for successfully creating a new resource — it signals specifically "a new resource was created" as opposed to `200 OK`'s generic "the request succeeded." Convention also suggests including a `Location` header pointing to the new resource's URL, though this project's minimal demo may skip that detail.

**Q: How does this hand-rolled routing compare to what Spring Boot's `@RestController` gives you?**
A: Conceptually identical (method + path → handler), but Spring automates almost everything here: it parses path variables/query parameters into method arguments automatically, (de)serializes JSON request/response bodies via Jackson, handles status codes via return types/annotations (`@ResponseStatus`, `ResponseEntity`), and manages the underlying server (embedded Tomcat/Netty) entirely — this project's manual version exists specifically to make visible everything Spring later hides behind annotations.

**Q: Is the JDK's `HttpServer` used in real production applications?**
A: Rarely for full production APIs — it lacks much of what a real framework provides (routing conveniences, JSON handling, middleware/filters, robust concurrency configuration, security features), though it's occasionally used for very small internal tools, health-check endpoints, or embedded admin interfaces where pulling in a full framework would be overkill. For anything resembling a real REST API, Spring Boot (or a similar framework) is the practical choice.
