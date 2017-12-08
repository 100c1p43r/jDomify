package pl.dusik.jdomify.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.jetty.websocket.api.RemoteEndpoint;
import pl.dusik.jdomify.data.ConnectionDTO;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

class JDomifyAgent {
    private static final long TIMEOUT = 5;
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private volatile RemoteEndpoint remote;
    private volatile CompletableFuture<String> future;

    public void setRemote(RemoteEndpoint remote) {
        this.remote = remote;
        future.complete("COMPLETED");
        this.future = null;
    }

    public void onText(String message) {
        if (future != null) {
            future.complete(message);
            future = null;
        }
    }

    public  void goTo(String url) throws IOException {
        send(new ConnectionDTO("go", url));
    }

    public void render() throws IOException {
        send(new ConnectionDTO("render"));
    }

    public  String getSource() throws IOException {
        createFuture();
        send(new ConnectionDTO("source"));
        try {
            return future.get(TIMEOUT, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            throw new IOException("Could not get page source.", e);
        }
    }

    CompletableFuture awaitFuture() {
        return createFuture();
    }

    private CompletableFuture<String> createFuture() {
        if (future != null) {
            throw new RuntimeException("Another operation already in progress");
        }
        return future = new CompletableFuture<>();
    }

    private void send(ConnectionDTO dto) throws IOException {
        remote.sendString(objectMapper.writeValueAsString(dto));
    }
}
