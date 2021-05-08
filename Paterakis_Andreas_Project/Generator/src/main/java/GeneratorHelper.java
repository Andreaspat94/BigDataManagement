import java.util.Locale;
import java.util.Map;
import java.util.Random;

public class GeneratorHelper {
    public static final String upper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static final String digits = "0123456789";
    public static final String lower = upper.toLowerCase(Locale.ROOT);
    public static final String alpha = upper + lower;
    public static final String STRING = "string";
    public static final String INTEGER = "int";
    public static final String FLOAT = "float";

    private Random randomGenerator = new Random();

    public Object pickValue(String type, int valueLength) {
        Object randomValue = null;
        if (type.equals(STRING)) {
            randomValue = "\"" + stringGenerator(valueLength) + "\"";
        } else if (type.equals(INTEGER)) {
            randomValue = integerGenerator(valueLength);
        } else if (type.equals(FLOAT)) {
            randomValue = floatGenerator();
        }
        return randomValue;
    }

    public String pickRandomKey(Map<String, String> keyFileMap) {
        Object[] keys = keyFileMap.keySet().toArray();
        return (String) keys[randomGenerator.nextInt(keys.length)];
    }

    /**
     * Generates a random string.
     *
     * @return the string
     */
    private String stringGenerator(int length) {
        Generator stringGenerator = new Generator(length, alpha);
        return stringGenerator.nextString();
    }

    /**
     * Generates a random integer.
     *
     * @return the integer
     * @throws NumberFormatException
     */
    private int integerGenerator(int length) throws NumberFormatException {
        Generator digitGenerator = new Generator(2, digits);
        return Integer.parseInt(digitGenerator.nextString());
    }

    /**
     * Generates a random float.
     *
     * @return the float
     * @throws NumberFormatException
     */
    private float floatGenerator() throws NumberFormatException {
        Generator digitGenerator = new Generator(2, digits);
        return Float.parseFloat(digitGenerator.nextString());
    }

}
