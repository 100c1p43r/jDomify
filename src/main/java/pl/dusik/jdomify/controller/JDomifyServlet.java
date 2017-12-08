package pl.dusik.jdomify.controller;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketAdapter;
import org.eclipse.jetty.websocket.servlet.WebSocketServlet;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;

public class JDomifyServlet extends WebSocketServlet {
    private final JDomifyAgent agent;

    public JDomifyServlet(JDomifyAgent agent) {
        this.agent = agent;
    }

    @Override
    public void configure(WebSocketServletFactory factory) {
        factory.getPolicy().setMaxTextMessageSize(20_000_000);
        factory.getPolicy().setMaxTextMessageBufferSize(20_000_000);
        factory.setCreator((req, res) -> {
            JDomifySocket socket = new JDomifySocket();
            return socket;
        });
    }

    class JDomifySocket extends WebSocketAdapter {

        @Override
        public void onWebSocketConnect(Session sess) {
            agent.setRemote(sess.getRemote());
        }

        @Override
        public void onWebSocketText(String message) {
            agent.onText(message);
        }

        @Override
        public void onWebSocketClose(int statusCode, String reason) {
            super.onWebSocketClose(statusCode, reason);
        }

        @Override
        public void onWebSocketError(Throwable cause) {
            super.onWebSocketError(cause);
            cause.printStackTrace(System.err);
        }
    }
}
