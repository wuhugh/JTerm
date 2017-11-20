/*
* JTerm - a cross-platform terminal
* Copyright (code) 2017 Sergix, NCSGeek
*
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
* You should have received a copy of the GNU General Public License
* along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

// package = folder :P
package jterm;

import jterm.command.Command;
import jterm.command.CommandException;
import jterm.command.CommandExecutor;
import jterm.gui.Terminal;
import jterm.io.InputHandler;
import jterm.util.Util;
import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class JTerm {
    private static final Map<String, CommandExecutor> COMMANDS = new HashMap<>();

    public static InputHandler inputHandler;

    public static final String VERSION = "0.6.1";
    public static final String PROMPT = ">> ";
    public static String LICENSE = "JTerm Copyright (C) 2017 Sergix, NCSGeek, chromechris\n"
            + "This program comes with ABSOLUTELY NO WARRANTY.\n"
            + "This is free software, and you are welcome to redistribute it\n"
            + "under certain conditions.\n";

    // Default value of getProperty("user.dir") is equal to the default directory set when the program starts
    // Global directory variable (use "cd" command to change)
    public static String currentDirectory = System.getProperty("user.dir");
    public static final String USER_HOME_DIR = System.getProperty("user.home");

    public static boolean IS_WIN;
    public static boolean IS_UNIX;

    static {
        setOS();
        initCommands();
    }

    public static BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
    private static Terminal terminal;
    private static boolean headless = false;

    public static void main(String[] args) {
        inputHandler = new InputHandler();
        if (args.length > 0 && args[0].equals("headless")) {
            headless = true;
            System.out.println(LICENSE);
            System.out.print(PROMPT);
            while (true) {
                inputHandler.process();
            }
        } else {
            terminal = new Terminal();
            terminal.setTitle("JTerm");
            terminal.setSize(720, 480);
            terminal.setVisible(true);
        }
    }

    public static void executeCommand(String options) {
        List<String> optionsArray = Util.getAsArray(options);

        if (optionsArray.size() == 0) {
            return;
        }

        String command = optionsArray.remove(0);
        if (!COMMANDS.containsKey(command)) {
            System.out.println("Command \"" + command + "\" is not available");
            return;
        }

        try {
            System.out.println();
            COMMANDS.get(command).execute(optionsArray);
        } catch (CommandException e) {
            System.err.println(e.getMessage());
        }
    }

    private static void initCommands() {
        Reflections reflections = new Reflections("jterm.command", new MethodAnnotationsScanner());
        Set<Method> methods = reflections.getMethodsAnnotatedWith(Command.class);

        for (Method method : methods) {
            method.setAccessible(true);
            Command command = method.getDeclaredAnnotation(Command.class);
            for (String commandName : command.name()) {
                CommandExecutor executor = new CommandExecutor()
                        .setCommandName(commandName)
                        .setSyntax(command.syntax())
                        .setMinOptions(command.minOptions())
                        .setCommand((List<String> options) -> {
                            try {
                                System.out.println(options);
                                method.invoke(null, options);
                            } catch (Exception e) {
                                System.err.println("Weird stuff...");
                            }
                        });
                COMMANDS.put(commandName, executor);
            }
        }
    }
    private static void setOS() {
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("windows")) {
            JTerm.IS_WIN = true;
        } else if ("linux".equals(os) || os.contains("mac") || "sunos".equals(os) || "freebsd".equals(os)) {
            JTerm.IS_UNIX = true;
        }
    }

    public static boolean isHeadless() {
        return headless;
    }

    public static Terminal getTerminal() {
        return terminal;
    }
}