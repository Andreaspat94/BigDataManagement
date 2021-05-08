import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Iterator;

public class Trie {
    private static final String NOT_FOUND = "NOT FOUND";
    private static final String DELETED = "DELETED";
    private static final String ERROR = "ERROR";
    private static final String OK = "OK";
    private static final String CURLY_BRACKET = "{";
    private final TrieNode root;
    private static ObjectMapper objectMapper = new ObjectMapper();

    public Trie() {
        this.root = new TrieNode();
    }

    public TrieNode getRoot() {
        return root;
    }

    public String get(String key, TrieNode rootNode) {
        String response = null;
        TrieNode current = rootNode;

        for (int i = 0; i < key.length(); i++) {
            char ch = key.charAt(i);

            TrieNode node = current.getChildren().get(ch);
            if (node == null) {
                response = NOT_FOUND;
                break;
            }

            if (node.isEndOfWord() && (i == key.length() - 1)) {
                response = node.getValue();
                break;
            }

            current = node;
        }
        if (response == null) {
            response = NOT_FOUND;
        }
        return response;
    }


    public String query(String query, TrieNode rootNode, int j) {
        String response = NOT_FOUND;
        TrieNode current = rootNode;

        String[] keys = query.split("\\.");
        String key = keys[j];

        for (int i = 0; i < key.length(); i++) {
            char ch = key.charAt(i);

            TrieNode node = current.getChildren().get(ch);
            if (node == null) {
                response = NOT_FOUND;
                break;
            }

            if (node.isEndOfWord() && (i == key.length() - 1)) {
                if (node.getChildren().size() != 0 && !(j == keys.length - 1)) {
                    response = query(query, node, ++j);
                } else {
                    response = node.getValue();
                }
            }

            current = node;
        }
        return response;
    }

    public String delete(String key) {
        String response = NOT_FOUND;
        TrieNode current = root;

        for (int i = 0; i < key.length(); i++) {
            char ch = key.charAt(i);

            TrieNode node = current.getChildren().get(ch);
            if (node == null) {
                break;
            }

            if (node.isEndOfWord() && (i == key.length() - 1)) {
                node.setEndOfWord(false);
                node.setValue(null);
                response = DELETED;
            }
            current = node;
        }
        return response;
    }

    public String insert(String key, String value, TrieNode rootNode) {
        try {
            TrieNode current = rootNode;
            for (int i = 0; i < key.length(); i++) {
                char ch = key.charAt(i);

                TrieNode node = current.getChildren().get(ch);
                if (node == null) {
                    node = new TrieNode();
                    current.getChildren().put(ch, node);
                }
                current = node;
            }

            current.setValue(value);
            current.setEndOfWord(true);

            TrieNode finalCurrent = current;
            if (value.substring(0, 1).equals(CURLY_BRACKET)) {

                JsonNode jsonNode = objectMapper.readTree(value);

                if (!jsonNode.isEmpty()) {
                    Iterator<String> fieldNames = jsonNode.fieldNames();

                    while (fieldNames.hasNext()) {
                        String fieldName = fieldNames.next();
                        String field = jsonNode.get(fieldName).toString();

                        insert(fieldName, field, finalCurrent);
                    }
                }
            }

            current.setChildren(finalCurrent.getChildren());

        } catch (Exception e) {

            return ERROR;
        }
        return OK;
    }
}
