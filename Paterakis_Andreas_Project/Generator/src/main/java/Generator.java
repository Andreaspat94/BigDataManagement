import java.security.SecureRandom;
import java.util.Objects;
import java.util.Random;

public class Generator {

    private final Random random;
    private final char[] buffer;
    private final char[] symbols;

    public Generator(int length, Random random, String symbols) {
        if (length < 1) throw new IllegalArgumentException();
        this.random = Objects.requireNonNull(random);
        this.symbols = symbols.toCharArray();
        this.buffer = new char[length];
    }

    /**
     * Create an alphanumeric strings from a secure generator.
     */
    public Generator(int length, String symbols) {
        this(length, new SecureRandom(), symbols);
    }

    /**
     * Generate a random string.
     */
    public String nextString() {
        for (int idx = 0; idx < buffer.length; ++idx)
            buffer[idx] = symbols[random.nextInt(symbols.length)];
        return new String(buffer);
    }
}
