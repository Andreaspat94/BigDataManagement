public class ServerService {
    private static final String GET = "GET";
    private static final String PUT = "PUT";
    private static final String QUERY = "QUERY";
    private static final String DELETE = "DELETE";
    private static final String WHITESPACE = " ";
    private static final String COLUMN = " : ";
    private static final String INVALID_REQUEST = "This type of request is invalid";

    public String handleRequest(String query, Trie trie) {
        String response;
        String[] splitQuery = query.split(WHITESPACE, 2);
        String request = splitQuery[0].trim();
        String value;
        String[] keyValuePair;
        String rootKey;

        if (request.equals(PUT)) {
            keyValuePair = splitQuery[1].split(":", 2);
            rootKey = keyValuePair[0].trim().replaceAll("^\"|\"$", "");
            if (!keyValuePair[1].isEmpty()) {
                value = keyValuePair[1].replaceAll(";", ",").trim();
            } else {
                value = null;
            }
            response = responseFactory(rootKey, trie.insert(rootKey, value, trie.getRoot()));

        } else if (request.equals(GET)) {
            rootKey = splitQuery[1].trim();
            response = responseFactory(rootKey, trie.get(rootKey, trie.getRoot()));

        } else if (request.equals(QUERY)) {
            String[] searchTerms = splitQuery[1].split("\\.");
            rootKey = searchTerms[0];
            if (searchTerms.length == 1) {
                response = responseFactory(splitQuery[1],trie.get(rootKey, trie.getRoot()));
            } else {
                response = responseFactory(splitQuery[1], trie.query(splitQuery[1], trie.getRoot(), 0));
            }
        } else if (request.equals(DELETE)) {
            rootKey = splitQuery[1].trim();
            response = responseFactory(splitQuery[1], trie.delete(rootKey));
        } else {
            response = INVALID_REQUEST;
        }

        return response;
    }

    private String responseFactory(String key, String trieResponse) {
        StringBuilder builder = new StringBuilder();
        builder.append(key).append(COLUMN).append(trieResponse);

        return builder.toString();
    }
}
