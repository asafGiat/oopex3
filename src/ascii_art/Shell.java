package ascii_art;

import image.Image;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

public class Shell {
    private Image image;

    private final Map<String, Function<String, String>> commandMap = new HashMap<>() {{
        put("chars", Shell.this::handleChars);
        put("add", Shell.this::handleAdd);
        put("remove", Shell.this::handleRemove);
        put("res", Shell.this::handleRes);
        put("reverse", Shell.this::handleReverse);
        put("output", Shell.this::handleOutput);
        put("asciiArt" , Shell.this::handleAsciiArt);
    }};
    private ProgramRun run;

    private String handleAsciiArt(String s) {
        return "null";
    }

    private String handleOutput(String s) {
        return "null";
    }

    private String handleReverse(String s) {
        return "null";
    }

    public Shell() {
    }

    public Shell(char[][] chars) {
    }

    public static void main(String[] args) {
    }

    public void run(String imageName) throws IOException {
        this.run = new ProgramRun(imageName);
        System.out.print(">>> ");
        String command = KeyboardInput.readLine();
        while (!command.startsWith("exit")) {
            try {
                String response = new String(handleCommand(command));
                System.out.println(response);
            }
            catch (Exception e) {
                System.out.println("Unsupported command.");
            }
            System.out.print(">>> ");
            command = KeyboardInput.readLine();
        }

    }

    private String handleCommand(String command) {
        for (Map.Entry<String, Function<String, String>> entry : commandMap.entrySet()) {
            if (command.startsWith(entry.getKey())) {
                return entry.getValue().apply(command);
            }
        }
        throw new UnsupportedOperationException("Not implemented yet");
    }

    private String handleChars(String command) {
        Set<Character> charset = run.getCharset();

        // TreeSet automatically sorts by natural order (ASCII value)
        java.util.TreeSet<Character> sortedChars = new java.util.TreeSet<>(charset);

        // Build space-separated string
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for (Character c : sortedChars) {
            if (!first) {
                result.append(" ");
            }
            result.append(c);
            first = false;
        }

        return result.toString();
    }

    private String handleAdd(String command) {
        try {
            Set<Character> charsSet = getCharsSetFromCommand(command);
            for (char c : charsSet) {
                run.addChar(c);
            }
            return "Characters added successfully";
        } catch (InvalidCommandException e) {
            return "Error: " + e.getMessage();
            //throw proper exception;
        }
    }

    private String handleRemove(String command) {
        try {
            Set<Character> charsSet = getCharsSetFromCommand(command);
            for (char c : charsSet) {
                run.removeChar(c);
            }
            return "Characters removed successfully";
        } catch (InvalidCommandException e) {
            return "Error: " + e.getMessage();
            //throw proper exception;
        }
    }

    private String handleRes(String command) {
        int currentRes = run.getResolution();
        final String message = "Resolution set to ";
        String[] parts = null;
        try{
            parts = parseCommand(command);
        } catch (InvalidCommandException e) {
            return message + Integer.toString(currentRes);
        }
        try {
            if (parts[1].equals("up")) {
                run.setResolution(currentRes * 2);
            } else if (parts[1].equals("down")) {
                run.setResolution(currentRes / 2);
            } else {
                throw new UnsupportedOperationException("Not implemented yet");
            }
            return message + Integer.toString(run.getResolution());
        }
        catch (ResolutionOutOfBoundsException e)
        {
            return "Did not change resolution due to exceeding boundaries.";
        }
    }

    /**
     * Parses a command string and validates it has at least the required number of parts.
     *
     * @param command the command string to parse
     * @return array of command parts split by whitespace
     * @throws InvalidCommandException if the command has fewer parts than required
     */
    private String[] parseCommand(String command) throws InvalidCommandException {
        String[] parts = command.trim().split("\\s+");

        if (parts.length < 2) {
            throw new InvalidCommandException("Invalid command format: missing argument");
        }

        return parts;
    }

    private Set<Character> getCharsSetFromCommand(String command) throws InvalidCommandException {
        String[] parts = parseCommand(command);

        String argument = parts[1];
        Set<Character> result = new java.util.HashSet<>();

        // Handle "all" keyword - all printable ASCII characters
        if (argument.equals("all")) {
            for (char c = ' '; c <= '~'; c++) {
                result.add(c);
            }
            return result;
        }

        // Handle "space" keyword
        if (argument.equals("space")) {
            result.add(' ');
            return result;
        }

        // Handle range format "x-y"
        if (argument.length() == 3 && argument.charAt(1) == '-') {
            char start = argument.charAt(0);
            char end = argument.charAt(2);

            // Add characters in range (handle both directions)
            if (start <= end) {
                for (char c = start; c <= end; c++) {
                    result.add(c);
                }
            } else {
                for (char c = end; c <= start; c++) {
                    result.add(c);
                }
            }
            return result;
        }

        // Handle single character
        if (argument.length() == 1) {
            result.add(argument.charAt(0));
            return result;
        }

        // Invalid format
        throw new InvalidCommandException("Invalid argument format: " + argument);
    }

}
