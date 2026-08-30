// Q: Using com.sun.net.httpserver.HttpServer (JDK built-in, no framework),
// build an in-memory contacts HTTP API on port 8080: GET /contacts (list),
// POST /contacts (create from a "name=X&phone=Y" body), DELETE
// /contacts?id=N (delete by id). Return proper status codes (200, 201, 400,
// 404, 405). main() starts the server, and — since interactively curling it
// isn't possible in an automated demo — also drives a few requests against
// itself over a real socket using HttpURLConnection to prove the routes
// work end-to-end, printing each response, before shutting the server down.

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

class Contact {
    int id;
    String name;
    String phone;

    Contact(int id, String name, String phone) {
        this.id = id;
        this.name = name;
        this.phone = phone;
    }

    String toResponseLine() {
        return id + "," + name + "," + phone;
    }
}

public class ContactHttpServer {
    static final List<Contact> contacts = new ArrayList<>();
    static final AtomicInteger nextId = new AtomicInteger(1);

    static Map<String, String> parseForm(String body) {
        Map<String, String> map = new HashMap<>();
        if (body == null || body.isBlank()) return map;
        for (String pair : body.split("&")) {
            String[] kv = pair.split("=", 2);
            String key = URLDecoder.decode(kv[0], StandardCharsets.UTF_8);
            String value = kv.length > 1 ? URLDecoder.decode(kv[1], StandardCharsets.UTF_8) : "";
            map.put(key, value);
        }
        return map;
    }

    static Map<String, String> parseQuery(URI uri) {
        return parseForm(uri.getRawQuery());
    }

    static void respond(HttpExchange exchange, int status, String body) throws IOException {
        byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(status, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }

    static void handleList(HttpExchange exchange) throws IOException {
        StringBuilder sb = new StringBuilder();
        for (Contact c : contacts) sb.append(c.toResponseLine()).append("\n");
        respond(exchange, 200, sb.toString());
    }

    static void handleCreate(HttpExchange exchange) throws IOException {
        String body;
        try (InputStream is = exchange.getRequestBody()) {
            body = new String(is.readAllBytes(), StandardCharsets.UTF_8);
        }
        Map<String, String> form = parseForm(body);
        if (!form.containsKey("name")) {
            respond(exchange, 400, "Bad Request: missing name\n");
            return;
        }
        Contact c = new Contact(nextId.getAndIncrement(), form.get("name"), form.getOrDefault("phone", ""));
        contacts.add(c);
        respond(exchange, 201, c.toResponseLine() + "\n");
    }

    static void handleDelete(HttpExchange exchange) throws IOException {
        Map<String, String> query = parseQuery(exchange.getRequestURI());
        if (!query.containsKey("id")) {
            respond(exchange, 400, "Bad Request: missing id\n");
            return;
        }
        int id = Integer.parseInt(query.get("id"));
        boolean removed = contacts.removeIf(c -> c.id == id);
        respond(exchange, removed ? 200 : 404, removed ? "Deleted\n" : "Not Found\n");
    }

    static String sendRequest(String method, String urlSuffix, String body) throws IOException {
        URI uri = URI.create("http://localhost:8080" + urlSuffix);
        HttpURLConnection conn = (HttpURLConnection) uri.toURL().openConnection();
        conn.setRequestMethod(method);
        if (body != null) {
            conn.setDoOutput(true);
            try (OutputStream os = conn.getOutputStream()) {
                os.write(body.getBytes(StandardCharsets.UTF_8));
            }
        }
        int status = conn.getResponseCode();
        InputStream stream = status < 400 ? conn.getInputStream() : conn.getErrorStream();
        String responseBody = stream == null ? "" : new String(stream.readAllBytes(), StandardCharsets.UTF_8);
        return status + " " + responseBody.replace("\n", " | ");
    }

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/contacts", exchange -> {
            try {
                switch (exchange.getRequestMethod()) {
                    case "GET" -> handleList(exchange);
                    case "POST" -> handleCreate(exchange);
                    case "DELETE" -> handleDelete(exchange);
                    default -> respond(exchange, 405, "Method Not Allowed\n");
                }
            } catch (Exception e) {
                respond(exchange, 500, "Internal Server Error: " + e.getMessage() + "\n");
            }
        });
        server.start();
        System.out.println("Server started on port 8080");

        try {
            System.out.println("GET /contacts (empty): " + sendRequest("GET", "/contacts", null));
            System.out.println("POST /contacts (Alice): " + sendRequest("POST", "/contacts", "name=Alice&phone=111-2222"));
            System.out.println("POST /contacts (Bob): " + sendRequest("POST", "/contacts", "name=Bob&phone=333-4444"));
            System.out.println("GET /contacts (two): " + sendRequest("GET", "/contacts", null));
            System.out.println("DELETE /contacts?id=1: " + sendRequest("DELETE", "/contacts?id=1", null));
            System.out.println("GET /contacts (one left): " + sendRequest("GET", "/contacts", null));
            System.out.println("POST /contacts (missing name): " + sendRequest("POST", "/contacts", "phone=555-0000"));
            System.out.println("DELETE /contacts?id=99 (missing): " + sendRequest("DELETE", "/contacts?id=99", null));
        } finally {
            server.stop(0);
            System.out.println("Server stopped");
        }
    }
}
