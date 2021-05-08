import java.io.*;
import java.util.*;

public class GeneratorService {

    public static final Random randomGenerator = new Random();

    private final GeneratorHelper helper = new GeneratorHelper();

    /**
     * Generates data
     *
     * @param numberOfLines  lines of data that will be generated
     * @param levelOfNesting maximum level on nesting. how many times in a line a value can have a set of key-values.
     * @param keysPerLevel   maximum number of keys inside each value/ per nesting level.
     * @param valueLength    the length of a string value.
     */
    public void generateData(String fileName,
                             Integer numberOfLines,
                             Integer levelOfNesting,
                             Integer keysPerLevel,
                             Integer valueLength) throws IOException {
        System.out.println("Generator activated..." + "\n"+ "-n: " + numberOfLines + "\n"
                + "-d: " + levelOfNesting + "\n"
                + "-m: " + keysPerLevel + "\n"
                + "-l: " + valueLength);

        validateInput(fileName, numberOfLines, levelOfNesting, keysPerLevel, valueLength);
        KeyFileReader keyFileReader = new KeyFileReader();

        try (FileWriter writer = new FileWriter("dataToIndex.txt")) {
            for (int i = 1; i <= numberOfLines; ++i) {
                Map<String, String> keyFileMap = keyFileReader.getKeyFileMap(fileName);
                if (keyFileMap.size() < keysPerLevel) {
                    throw new IOException("-m value should be less than the number of elements of txt file(" + keyFileMap.size() + ")");
                }
                String key = "\"person" + i +"\"";
                Map<String, Object> data = generateEachBlockTest(keyFileMap, levelOfNesting, keysPerLevel, valueLength, key);

                writer.write(key + " : " + data + "\n");
            }
        }
        // format data
        dataFormat("dataToIndex.txt", "=", " : ");
        dataFormat("dataToIndex.txt", ",", " ;");
    }

    private void dataFormat(String keyFile, String oldElement, String newElement) throws IOException {

        File fileToBeModified = new File(keyFile);
        String oldContent = "";
        BufferedReader reader = null;
        FileWriter writer = null;

        try {
            reader = new BufferedReader(new FileReader(fileToBeModified));

            //Reading all the lines of input text file into oldContent
            String line = reader.readLine();

            while (line != null) {
                oldContent = oldContent + line + System.lineSeparator();

                line = reader.readLine();
            }

            //Replacing oldString with newString in the oldContent
            String newContent = oldContent.replaceAll(oldElement, newElement);

            //Rewriting the input text file with newContent
            writer = new FileWriter(fileToBeModified);
            writer.write(newContent);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                //Closing the resources
                reader.close();
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private void validateInput(String fileName,
                               Integer numberOfLines, Integer levelOfNesting,
                               Integer keysPerLevel, Integer valueLength) {
        if (fileName == null ||
                numberOfLines == null ||
                levelOfNesting == null ||
                keysPerLevel == null ||
                valueLength == null) {
            System.out.println("bad data. parameter values can not be empty");
            System.exit(0);
        }

    }

    public Map<String, Object> generateEachBlockTest(Map<String, String> keyFileMap,
                                                     int levelOfNesting,
                                                     int keysPerLevel,
                                                     int valueLength,
                                                     String key) {
        int nesting = levelOfNesting;

        Map<String, Object> data = new HashMap<>();

        //random key-value sets number;
        int m = randomGenerator.nextInt(keysPerLevel);
        if (m == 0) {
            return data;
        }

        if (nesting == 0 && !keyFileMap.isEmpty()) {
            for (int j = 0; j < m; j++) {
                createKeyValueSet(keyFileMap, data, valueLength);
            }
            data.put(key, data);
            return data;
        }
        //if there is nesting...
        Set<Integer> setWithNestedMaps = new HashSet<>();

        // random # of KV sets that have nested maps.
        int numberOfNestedValues = randomGenerator.nextInt(m);
        for (int i = 0; i < numberOfNestedValues; i++) {
            setWithNestedMaps.add(randomGenerator.nextInt(m));
        }

        for (int j = 1; j <= m; j++) {
            if (keyFileMap.isEmpty()) {
                break;
            }
            if (keyFileMap.size() == 1) {
                createKeyValueSet(keyFileMap, data, valueLength);
                break;
            }
            if (!setWithNestedMaps.contains(j) || nesting == 0) {
                createKeyValueSet(keyFileMap, data, valueLength);
            } else {

                //decreasing level of nesting
                --nesting;
                if (keyFileMap.size() > 2) {
                    createKeyValueSetWithMapObject(keyFileMap, data, valueLength, nesting, m);
                }
            }
        }
        return data;
    }

    private void createKeyValueSet(Map<String, String> keyFileMap, Map<String, Object> map, int valueLength) {
        if (keyFileMap.keySet().size() == 0) {
            return;
        }
        String key = helper.pickRandomKey(keyFileMap);
        String type = keyFileMap.get(key);
        Object value = helper.pickValue(type, valueLength);
        if (map.containsKey(key)) {
            return;
        }
        map.put(key, value);
        keyFileMap.remove(key);
    }

    private void createKeyValueSetWithMapObject(Map<String, String> keyFileMap, Map<String, Object> map, int valueLength, int nesting, int keysPerLevel) {
        final Map<String, Object> nestedMap = new HashMap<>();
        String key = helper.pickRandomKey(keyFileMap);
        keyFileMap.remove(key);
        Object value = generateEachBlockTest(keyFileMap, nesting, keysPerLevel, valueLength, key);
        nestedMap.put(key, value);

        if (keyFileMap.isEmpty()) {
            return;
        }
        String nestedKey = helper.pickRandomKey(keyFileMap);
        map.put(
                nestedKey,
                nestedMap
        );
        keyFileMap.remove(nestedKey);
    }
}
