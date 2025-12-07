package ascii_art;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

public class Shell {
    private final Map<String, Function<String, String>> commandMap = new HashMap<>() {{
        put("chars", Shell.this::handleChars);
        put("add", Shell.this::handleAdd);
        put("remove", Shell.this::handleRemove);
        put("res", Shell.this::handleRes);
        put("reverse", Shell.this::handleReverse);
        put("output", Shell.this::handleOutput);
        put("asciiArt" , Shell.this::handleAsciiArt);
    }};

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

    public void run(String imageName) {
        System.out.print(">>> ");
        String command = KeyboardInput.readLine();
        while (!command.startsWith("exit")) {
            try {
                String response = new String(handleCommand(command));
                System.out.println(response);
            }
            catch (UnsupportedOperationException e) {
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
        //todo: get the chars list from imagecharmatching
        return "this is the chars list...(placeholder)";
    }

    private String handleAdd(String command) {
        //todo: add char to chars list
        Set<Character> charsSet = getCharsSetFromCommand(command);
        return "added char...(placeholder)";
    }

    private String handleRemove(String command) {
        Set<Character> charsSet = getCharsSetFromCommand(command);
        return "removed char...(placeholder)";
    }

    private String handleRes(String command) {
        //todo: implement resolution handling
        throw new UnsupportedOperationException("Not implemented yet");
    }
    private Set<Character> getCharsSetFromCommand(String command) {
        //todo: implement
        return null;
    }

}
