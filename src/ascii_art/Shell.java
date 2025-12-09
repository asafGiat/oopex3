package ascii_art;

import image.Image;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

public class Shell {
    // Command names
    private static final String CMD_CHARS = "chars";
    private static final String CMD_ADD = "add";
    private static final String CMD_REMOVE = "remove";
    private static final String CMD_RES = "res";
    private static final String CMD_REVERSE = "reverse";
    private static final String CMD_OUTPUT = "output";
    private static final String CMD_ASCII_ART = "asciiArt";
    private static final String CMD_EXIT = "exit";

    // Command arguments
    private static final String ARG_UP = "up";
    private static final String ARG_DOWN = "down";
    private static final String ARG_ALL = "all";
    private static final String ARG_SPACE = "space";

    // Special characters
    private static final char RANGE_SEPARATOR = '-';
    private static final char SPACE_CHAR = ' ';
    private static final char TILDE_CHAR = '~';
    private static final char PRINTABLE_ASCII_START = ' ';
    private static final char PRINTABLE_ASCII_END = '~';

    // Numbers
    private static final int RESOLUTION_MULTIPLIER = 2;
    private static final int MIN_COMMAND_PARTS = 2;
    private static final int RANGE_FORMAT_LENGTH = 3;
    private static final int RANGE_SEPARATOR_INDEX = 1;
    private static final int RANGE_START_INDEX = 0;
    private static final int RANGE_END_INDEX = 2;
    private static final int SINGLE_CHAR_LENGTH = 1;
    private static final int COMMAND_ARG_INDEX = 1;

    // Messages
    private static final String MSG_INSUFFICIENT_CHARS = "Did not execute. Charset is too small.";
    private static final String MSG_OUTPUT_FORMAT_ERROR = "Did not change output method due to incorrect format.";
    private static final String MSG_CHARS_ADDED = "Characters added successfully";
    private static final String MSG_CHARS_REMOVED = "Characters removed successfully";
    private static final String MSG_RESOLUTION_SET = "Resolution set to ";
    private static final String MSG_RESOLUTION_BOUNDS_ERROR = "Did not change resolution due to exceeding boundaries.";
    private static final String MSG_RESOLUTION_SYNTAX_ERROR = "Did not change resolution due to incorrect format." +
            "boundaries.";
    private static final String MSG_INCORRECT_COMMAND = "Did not execute due to incorrect command.";
    private static final String MSG_ERROR_PREFIX = "Error: ";
    private static final String MSG_INVALID_ARG_FORMAT = "Invalid argument format: ";
    private static final String MSG_INVALID_CMD_FORMAT = "Invalid command format: missing argument";
    private static final String MSG_SHELL_IO_ERROR = "Failed to run shell due to IO error: ";
    private static final String MSG_PROMPT = ">>> ";
    private static final String MSG_EMPTY = "";

    // Formatting
    private static final String SPACE_SEPARATOR = " ";
    private static final String WHITESPACE_REGEX = "\\s+";

    private Image image;

    private final Map<String, Function<String, String>> commandMap = new HashMap<>() {{
        put(CMD_CHARS, Shell.this::handleChars);
        put(CMD_ADD, Shell.this::handleAdd);
        put(CMD_REMOVE, Shell.this::handleRemove);
        put(CMD_RES, Shell.this::handleRes);
        put(CMD_REVERSE, Shell.this::handleReverse);
        put(CMD_OUTPUT, Shell.this::handleOutput);
        put(CMD_ASCII_ART, Shell.this::handleAsciiArt);
    }};
    private ProgramRun run;

    private String handleAsciiArt(String s) {
        try {
            run.run();
            return MSG_EMPTY;
        }
        catch (InsufficientCharsException e) {
            return MSG_INSUFFICIENT_CHARS;
        }
    }

    private String handleOutput(String command) {
        try {
            String[] parts = parseCommand(command);
            String outputType = parts[COMMAND_ARG_INDEX];
            run.setAsciiOutput(outputType);
        } catch (InvalidCommandException e) {
            return MSG_OUTPUT_FORMAT_ERROR;
        }
        return MSG_EMPTY;
    }

    private String handleReverse(String s) {
        run.toggleReverse();
        return MSG_EMPTY;
    }

    public Shell() {
    }

    public Shell(char[][] chars) {
    }

    public static void main(String[] args) {
        Shell shell = new Shell();
        try {
            shell.run("examples/cat.jpeg");
        } catch (IOException e) {
            System.out.println(MSG_SHELL_IO_ERROR + e.getMessage());
        }
    }

    public void run(String imageName) throws IOException {
        this.run = new ProgramRun(imageName);
        System.out.print(MSG_PROMPT);
        String command = KeyboardInput.readLine();
        while (!command.startsWith(CMD_EXIT)) {
            String response = new String(handleCommand(command));
            if(!response.isEmpty()) {
                System.out.println(response);
            }
            System.out.print(MSG_PROMPT);
            command = KeyboardInput.readLine();
        }

    }

    private String handleCommand(String command) {
        for (Map.Entry<String, Function<String, String>> entry : commandMap.entrySet()) {
            if (command.equals(entry.getKey()) || command.startsWith(entry.getKey()+SPACE_SEPARATOR)) {
                return entry.getValue().apply(command);

            }
        }
        return MSG_INCORRECT_COMMAND;
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
                result.append(SPACE_SEPARATOR);
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
            return MSG_CHARS_ADDED;
        } catch (InvalidCommandException e) {
            return "Did not add due to incorrect format.";
        }
    }

    private String handleRemove(String command) {
        try {
            Set<Character> charsSet = getCharsSetFromCommand(command);
            for (char c : charsSet) {
                run.removeChar(c);
            }
            return MSG_CHARS_REMOVED;
        } catch (InvalidCommandException e) {
            return "Did not remove due to incorrect format.";
        }
    }

    private String handleRes(String command) {
        int currentRes = run.getResolution();
        final String message = MSG_RESOLUTION_SET;
        String[] parts = null;
        try{
            parts = parseCommand(command);
        } catch (InvalidCommandException e) {
            return message + Integer.toString(currentRes);
        }
        try {
            if (parts[COMMAND_ARG_INDEX].equals(ARG_UP)) {
                run.setResolution(currentRes * RESOLUTION_MULTIPLIER);
            } else if (parts[COMMAND_ARG_INDEX].equals(ARG_DOWN)) {
                run.setResolution(currentRes / RESOLUTION_MULTIPLIER);
            } else {
                return MSG_RESOLUTION_SYNTAX_ERROR;
            }
            return message + Integer.toString(run.getResolution());
        }
        catch (ResolutionOutOfBoundsException e)
        {
            return MSG_RESOLUTION_BOUNDS_ERROR;
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
        String[] parts = command.trim().split(WHITESPACE_REGEX);

        if (parts.length < MIN_COMMAND_PARTS) {
            throw new InvalidCommandException(MSG_INVALID_CMD_FORMAT);
        }

        return parts;
    }

    private Set<Character> getCharsSetFromCommand(String command) throws InvalidCommandException {
        String[] parts = parseCommand(command);

        String argument = parts[COMMAND_ARG_INDEX];
        Set<Character> result = new java.util.HashSet<>();

        // Handle "all" keyword - all printable ASCII characters
        if (argument.equals(ARG_ALL)) {
            for (char c = PRINTABLE_ASCII_START; c <= PRINTABLE_ASCII_END; c++) {
                result.add(c);
            }
            return result;
        }

        // Handle "space" keyword
        if (argument.equals(ARG_SPACE)) {
            result.add(SPACE_CHAR);
            return result;
        }

        // Handle range format "x-y"
        if (argument.length() == RANGE_FORMAT_LENGTH && argument.charAt(RANGE_SEPARATOR_INDEX) == RANGE_SEPARATOR) {
            char start = argument.charAt(RANGE_START_INDEX);
            char end = argument.charAt(RANGE_END_INDEX);

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
        if (argument.length() == SINGLE_CHAR_LENGTH) {
            result.add(argument.charAt(RANGE_START_INDEX));
            return result;
        }

        // Invalid format
        throw new InvalidCommandException(MSG_INVALID_ARG_FORMAT + argument);
    }

}
