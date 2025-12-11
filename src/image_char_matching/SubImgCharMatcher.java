package image_char_matching;
import java.util.*;

/**
 * A class that matches characters to brightness levels based on their sub-image representations.
 * It allows adding and removing characters, and retrieves the best matching character for a given
 * brightness.
 */
public class SubImgCharMatcher {

    private final HashMap<Character, Double> brightness;
    private TreeMap<Double, TreeSet<Character>> normalizedCharMap;
    private  double maxBrightness;
    private  double minBrightness;

    /**
     * Constructor that initializes the character matcher with a given charset.
     * It calculates the brightness for each character and builds a normalized map.
     * @param charset array of characters to be used in the matcher
     */
    public SubImgCharMatcher(char[] charset){
        this.brightness = new HashMap<Character, Double>();
        maxBrightness=0.0;
        minBrightness=1.0;
        Double value;
        for (char c:charset) {
            value = brightnessCal(CharConverter.convertToBoolArray(c));
            if (value<minBrightness){
                minBrightness=value;
            }
            if (value>maxBrightness){
                maxBrightness=value;
            }
            brightness.put(c,value);
        }
        rebuildNormalizedMap();
    }

    /**
     * Given a brightness value, returns the character that best matches that brightness.
     * @param brightness the brightness value to match
     * @return the character that best matches the given brightness
     */
    public char getCharByImageBrightness(double brightness){
        Map.Entry<Double, TreeSet<Character>> entryCeiling = normalizedCharMap.ceilingEntry(brightness);
        Map.Entry<Double, TreeSet<Character>> entryFloor = normalizedCharMap.floorEntry(brightness);

        // Handle edge cases where one or both entries are null
        if (entryFloor == null && entryCeiling == null) {
            // Should not happen if map is properly initialized, but handle gracefully
            return normalizedCharMap.firstEntry().getValue().first();
        }
        if (entryFloor == null) {
            return entryCeiling.getValue().first();
        }
        if (entryCeiling == null) {
            return entryFloor.getValue().first();
        }

        // Both exist, find the closest
        if (brightness - entryFloor.getKey() <= entryCeiling.getKey() - brightness) {
            return entryFloor.getValue().first();
        } else {
            return entryCeiling.getValue().first();
        }
    }

    /**
     * Adds a character to the matcher, updating brightness calculations and normalized map.
     * @param c the character to add
     */
    public void addChar(char c){
        Double value = brightnessCal(CharConverter.convertToBoolArray(c));
        brightness.put(c,value);
        if (value<minBrightness){
            minBrightness=value;
            rebuildNormalizedMap();
        }
        if (value>maxBrightness){
            maxBrightness=value;
            rebuildNormalizedMap();
        }
    }

    /**
     * Removes a character from the matcher, updating brightness calculations and normalized map.
     * @param c the character to remove
     */
    public void removeChar(char c){
        Double value = brightness.get(c);
        brightness.remove(c);
        if (value!=null && (value.equals(minBrightness) || value.equals(maxBrightness))){
            //recalculate min and max
            minBrightness=1.0;
            maxBrightness=0.0;
            for (Double v:brightness.values()) {
                if (v<minBrightness){
                    minBrightness=v;
                }
                if (v>maxBrightness){
                    maxBrightness=v;
                }
            }
            rebuildNormalizedMap();
        }
    }

    //--private methods--//

    private Double brightnessCal(boolean [][] img){
        Double counter = 0.0;

        for (boolean[] booleans : img) {
            for (boolean aBoolean : booleans) {
                if (aBoolean) {
                    counter++;
                }
            }
        }
        counter = counter/(img.length*img[1].length);  //normalized the number of turned pixels
        return counter ;
    }

    private void rebuildNormalizedMap() {

        double range = maxBrightness - minBrightness;
        TreeMap<Double, TreeSet<Character>> newNormalizedMap = new TreeMap<>();

        for (HashMap.Entry<Character, Double> entry : this.brightness.entrySet()) {
            char c = entry.getKey();
            double rawBrightness = entry.getValue();
            double newCharBrightness;

            // Handle the case where all characters have the same brightness (range = 0)
            if (range == 0) {
                newCharBrightness = 0.0;  // or any fixed value, since all chars are equal
            } else {
                newCharBrightness = (rawBrightness - minBrightness) / range;
            }
            TreeSet<Character> charSet = newNormalizedMap.get(newCharBrightness);

            if (charSet == null) {
                charSet = new TreeSet<>();
                newNormalizedMap.put(newCharBrightness, charSet);
            }
            charSet.add(c);
        }
        this.normalizedCharMap = newNormalizedMap;
    }

    public static void main(String[] args) {
        char[] charset = {'A', 'B', 'D', 'E'};
        SubImgCharMatcher matcher = new SubImgCharMatcher(charset);
                matcher.removeChar('C');
        System.out.println("After removing 'C':");
        for (Map.Entry<Double, TreeSet<Character>> entry : matcher.normalizedCharMap.entrySet()) {
            System.out.println("Brightness: " + entry.getKey() + " Characters: " + entry.getValue());
        }
        System.out.println(matcher.getCharByImageBrightness(0.7));
        matcher.addChar('C');
        System.out.println("After adding 'C':");
        for (Map.Entry<Double, TreeSet<Character>> entry : matcher.normalizedCharMap.entrySet()) {
            System.out.println("Brightness: " + entry.getKey() + " Characters: " + entry.getValue());
        }
        System.out.println(matcher.getCharByImageBrightness(0.7));
    }
}