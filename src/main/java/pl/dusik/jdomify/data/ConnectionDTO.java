package pl.dusik.jdomify.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ConnectionDTO {
    private String command;
    private List<String> args;

    public ConnectionDTO(String command) {
        this.command = command;
        args = Collections.emptyList();
    }

    public ConnectionDTO(String command, String... args) {
        this.command = command;
        this.args = Arrays.asList(args);
    }

    public String getCommand() {
        return command;
    }

    public List<String> getArgs() {
        return args;
    }
}
