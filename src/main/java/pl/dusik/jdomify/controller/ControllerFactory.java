package pl.dusik.jdomify.controller;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.concurrent.CompletableFuture;

import static java.util.concurrent.TimeUnit.SECONDS;

public class ControllerFactory {
    private static long TIMEOUT = 15;

    public JDomifyController create() throws IOException {
        Server server = new Server();
        ServerConnector serverConnector = new ServerConnector(server);
        server.addConnector(serverConnector);

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        server.setHandler(context);

        JDomifyAgent agent = new JDomifyAgent();
        JDomifyController controller = new JDomifyController(agent);
        ServletHolder holder = new ServletHolder("ws-events", new JDomifyServlet(agent));
        context.addServlet(holder, "/events/*");
        CompletableFuture future = agent.awaitFuture();

        try {
            server.start();
            System.out.println("Server started on port " + server.getURI().getPort());
            server.dump(System.err);
            runPhantomJs(server.getURI().getPort());
            future.get(TIMEOUT, SECONDS);
        } catch (Exception e) {
            e.printStackTrace(System.err);
            throw new IOException("Could not create jDomify controller", e);
        }

        if (!future.isDone()) {
            throw new IOException("phantomjs stub did not connect");
        }


        return controller;
    }

    void runPhantomJs(int port) throws IOException {
        URL remoteJs = ControllerFactory.class.getResource("/remote.js");
        Process process = Runtime.getRuntime().exec(
                String.format("phantomjs %s %d",  remoteJs.getFile(), port));
        InputStream is = process.getInputStream();
        new Thread(() -> {
            try {
                while(true) {
                    if (is.available() > 0) {
                        byte[] buffer = new byte[is.available()];
                        is.read(buffer);
                        System.out.print(new String(buffer));
                    }
                    Thread.sleep(100);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}
