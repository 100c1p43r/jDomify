package pl.dusik.jdomify.controller;

import java.io.IOException;

public class JDomifyController {
    private final JDomifyAgent agent;

    public JDomifyController(JDomifyAgent agent) {
        this.agent = agent;
    }

    public void goTo(String url) throws IOException {
        agent.goTo(url);
    }

    public String getSource() throws IOException {
        return agent.getSource();
    }

    public void render() throws IOException {
        agent.render();
    }
}
