package de.denkspuren.lvp;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;
import java.util.Random;
import java.util.stream.Collectors;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

public class LiveView {
    public final HttpServer server;
    final int port;
    static int defaultPort = 50_001;
    static final String index = "/web/index.html";
    static Map<Integer,LiveView> views = new ConcurrentHashMap<>();
    List<String> paths = new ArrayList<>();

    static void setDefaultPort(int port) { defaultPort = port != 0 ? Math.abs(port) : 50_001; }
    static int getDefaultPort() { return defaultPort; }

    public Map<String, HttpExchange> sseClientConnections;

    // lock required to temporarily block processing of `SSEType.LOAD`
    Lock lock = new ReentrantLock();
    Condition loadEventOccurredCondition = lock.newCondition();
    boolean loadEventOccured = false;

    public static LiveView onPort(int port) {
        port = Math.abs(port);
        try {
            if (!views.containsKey(port))
                views.put(port, new LiveView(port));
            return views.get(port);
        } catch (IOException e) {
            System.err.printf("Error starting Server: %s\n", e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public static LiveView onPort() { return onPort(defaultPort); }

    private LiveView(int port) throws IOException {
        this.port = port;
        sseClientConnections = new ConcurrentHashMap<>();

        server = HttpServer.create(new InetSocketAddress("localhost", port), 0);
        System.out.println("Open http://localhost:" + port + " in your browser");

        // loaded-Request to signal successful processing of SSEType.LOAD
        server.createContext("/loaded", exchange -> {
            if (!exchange.getRequestMethod().equalsIgnoreCase("post")) {
                exchange.sendResponseHeaders(405, -1); // Method Not Allowed
                return;
            }
            exchange.sendResponseHeaders(200, 0);
            exchange.close();
            lock.lock();
            try { // try/finally pattern for locks
                loadEventOccured = true;
                loadEventOccurredCondition.signalAll();
            } finally {
                lock.unlock();
            }
        });

        server.createContext("/close", exchange -> {
            if (!exchange.getRequestMethod().equalsIgnoreCase("delete")) {
                exchange.sendResponseHeaders(405, -1); // Method Not Allowed
                return;
            }
            
            String id = exchange.getRequestURI().getQuery().substring(3);
            System.out.println("Closing: " + id);
            sseClientConnections.get(id).close();
            sseClientConnections.remove(id);
            
            exchange.sendResponseHeaders(200, 0);
            exchange.close();
            
        });

        // SSE context
        server.createContext("/events", exchange -> {
            if (!exchange.getRequestMethod().equalsIgnoreCase("get")) {
                exchange.sendResponseHeaders(405, -1); // Method Not Allowed
                return;
            }
            exchange.getResponseHeaders().add("Content-Type", "text/event-stream");
            exchange.getResponseHeaders().add("Cache-Control", "no-cache");
            exchange.getResponseHeaders().add("Connection", "keep-alive");
            exchange.sendResponseHeaders(200, 0);
            
            String id = exchange.getRequestURI().getQuery().substring(3);
            System.out.println("New Connection: " + id);

            sseClientConnections.put(id, exchange);
        });

        // initial html site
        server.createContext("/", exchange -> {
            if (!exchange.getRequestMethod().equalsIgnoreCase("get")) {
                exchange.sendResponseHeaders(405, -1); // Method Not Allowed
                return;
            }
            final String path = exchange.getRequestURI().getPath().equals("/") ? index : exchange.getRequestURI().getPath();

            try (final InputStream stream = LiveView.class.getResourceAsStream(path)) {
                final byte[] bytes = stream.readAllBytes();
                exchange.getResponseHeaders().add("Content-Type", Files.probeContentType(Path.of(path)) + "; charset=utf-8");
                exchange.sendResponseHeaders(200, bytes.length);
                exchange.getResponseBody().write(bytes);
                exchange.getResponseBody().flush();
            } finally {
                exchange.close();
            }
        });

        server.setExecutor(Executors.newFixedThreadPool(5));
        server.start();
    }

    public void sendServerEvent(SSEType sseType, String data) {
        List<String> deadConnections = new ArrayList<>();
        for (String id : sseClientConnections.keySet()) {
            HttpExchange connection = sseClientConnections.get(id);
            System.out.println("SSE-Connection: " + connection.getRemoteAddress() + " with ID: " + id);
            if (sseType == SSEType.LOAD) {
                lock.lock();
                loadEventOccured = false; // NEU
                System.out.println("lock.lock(): " + java.time.Instant.now() + " " + data);
            }
            try {
                byte[] binaryData = data.getBytes(StandardCharsets.UTF_8);
                String base64Data = Base64.getEncoder().encodeToString(binaryData);
                String message = "data: " + sseType + ":" + base64Data + "\n\n";
                connection.getResponseBody()
                          .write(message.getBytes());
                connection.getResponseBody().flush();
                if (sseType == SSEType.LOAD) {
                    loadEventOccurredCondition.await(10_000, TimeUnit.MILLISECONDS);
                    System.out.println("await(): " + java.time.Instant.now() + " " + data);
                    if (loadEventOccured) paths.add(data);
                    else System.err.println("LOAD-Timeout: " + data);
                }
            } catch (IOException e) {
                deadConnections.add(id);
            } catch (InterruptedException e) {
                System.err.println("LOAD-Interruption: " + data + ", " + e);
            } finally {
                if (sseType == SSEType.LOAD) {
                    // loadEventOccured = false; // REMOVED
                    lock.unlock();
                }
            }
        }
        
        for (String id : deadConnections) {
            sseClientConnections.get(id).close();
            sseClientConnections.remove(id);
        }
    }

    public void createResponseContext(String path, Consumer<String> delegate) {
        createResponseContext(path, delegate, "-1");
    }

    public void createResponseContext(String path, Consumer<String> delegate, String id) {
        server.createContext(path, exchange -> {
            if (!exchange.getRequestMethod().equalsIgnoreCase("post")) {
                exchange.sendResponseHeaders(405, -1); // Method Not Allowed
                return;
            }

            String content_length = exchange.getRequestHeaders().getFirst("Content-length");
            if (content_length == null) {
                exchange.sendResponseHeaders(400, -1);
                return;
            }

            try {
                int length = Integer.parseInt(content_length);
                byte[] data = new byte[length];
                exchange.getRequestBody().read(data);
                delegate.accept(new String(data));
                sendServerEvent(SSEType.RELEASE, id);
            } catch (NumberFormatException e) {
                exchange.sendResponseHeaders(400, -1);
                return;
            }

            exchange.sendResponseHeaders(200, 0);
            exchange.close();
        });
    }

    public void stop() {
        sseClientConnections.clear();
        views.remove(port);
        server.stop(0);
    }

    public static void shutdown() {
        views.forEach((k, v) -> v.stop());
    }
}