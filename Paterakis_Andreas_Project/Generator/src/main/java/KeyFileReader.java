import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class KeyFileReader {
    public Map<String, String> getKeyFileMap(String fileName) {
        Scanner scanner = null;
        Map<String, String> keyMap = new HashMap<>();
        try {
            scanner = new Scanner(new FileReader(fileName));
            scanner.useDelimiter(",");
            while(scanner.hasNextLine()) {
                String key = scanner.next();
                scanner.skip(scanner.delimiter());
                String value = String.valueOf(scanner.nextLine());
                keyMap.put(key,value);

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
         } finally {
            if (scanner != null) {
                scanner.close();
            }
        }
        return keyMap;
    }
}
