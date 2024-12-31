
package util.commands;

import java.util.LinkedList;
import java.util.Queue;

public class CommandInvoker {
    private Queue<command> commandQueue;

    public CommandInvoker() {
        this.commandQueue = new LinkedList<>();
    }

    public void addCommand(command command) {
        commandQueue.add(command);
    }

    public void executeCommands() {
        while (!commandQueue.isEmpty()) {
            command command = commandQueue.poll();
            command.execute();
        }
    }
}
