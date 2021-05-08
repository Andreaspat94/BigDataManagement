import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class BrokerService {
    private static Random random = new Random();
    private static final String BAD_DATA = "-k value is bigger than the number of server ports stated in the file.";
    private static final String PUT = "PUT";
    private static final String BRACKET_PUT = "(PUT)";
    private static final String NO_SERVER_PORTS = "Failed to find server ports";
    private static final String WHITESPACE = " ";
    private static final String ERROR = "ERROR occurred while putting data: ";
    private static final String NOT_FOUND = "NOT FOUND";
    private static final String DELETED = "DELETED";
    private static final String INVALID_REQUEST = "This type of request is invalid";
    private static final String SHUT_DOWN = "\nBroker is shutting down...";
    private static final String SERVER_FILE_ERROR = "Can not find server addresses. A file name needs to be specified.";
    private static final String DATA_TO_INDEX_FILE_ERROR = "Can not find data to index. A file name needs to be specified.";

    public void constructBroker(String serverFile, String dataToIndex, Integer serversToReceiveQuery)
            throws IOException {
        // validate data
        final boolean serverExists = validateData(serverFile, dataToIndex, serversToReceiveQuery);
        if (!serverExists) {
            serversToReceiveQuery = 1;
        }
        //making a list with server ports
        Scanner scanner = new Scanner((new FileReader(serverFile)));
        List<Integer> listofPorts = new ArrayList<>();
        String input;
        while (scanner.hasNextLine()) {
            input = scanner.nextLine();
            String[] array = input.split(":");
            Integer port = Integer.parseInt(array[1].trim());
            if (!listofPorts.contains(port)) {
                listofPorts.add(port);
            }
        }
        if (listofPorts.size() == 0) {
            System.out.println(NO_SERVER_PORTS + SHUT_DOWN);
            System.exit(0);
        }

        // validate if serverFile has the same number of ports with serversToReceiveQuery
        if (listofPorts.size() < serversToReceiveQuery) {
            System.out.println(BAD_DATA + SHUT_DOWN);
            System.exit(0);
        }

        //deploy socket connections
        List<Broker> brokerList = deploy(listofPorts);

        //retrieve data from txt file.
        List<String> dataList = retrieveDataFromFile(dataToIndex);

        //send the data to servers.
        putDataToServers(dataList, brokerList, serversToReceiveQuery);

        //receive requests from user.
        receiveRequests(brokerList, serversToReceiveQuery);

    }

    private void receiveRequests(List<Broker> brokerList, int serversToReceiveQuery) {
        Scanner scanner = new Scanner(System.in);

        int initialServers = brokerList.size();
        String query;
        String response;
        List<String> responseList = new ArrayList<>();

        do {
            System.out.println("Enter query to be sent: ");
            query = scanner.nextLine();
            boolean brokenServer = false;
            boolean found = false;
            boolean invalid = false;
            boolean deleted = false;
            String memberDeleted = null;
            for (Broker broker : brokerList) {
                try {
                    response = broker.sendMessage(query);
                    if (response.equals(INVALID_REQUEST)) {
                        invalid = true;
                        break;
                    }
                    if (!responseList.contains(response)) {
                        responseList.add(response);
                        boolean checkIfResponseIsNotFound = response.substring(response.length() - 9).equals(NOT_FOUND);
                        boolean checkIfResponseIsDeleted = response.substring(response.length() - 7).equals(DELETED);
                        if ((!checkIfResponseIsNotFound) && !checkIfResponseIsDeleted) {
                            found = true;
                            responseList.set(0, response);
                            break;
                        } else if (checkIfResponseIsDeleted) {
                            deleted = true;
                            memberDeleted = response;
                        }
                    }
                } catch (IOException exception) {
                    brokerList.remove(broker);
                    int serversDown = initialServers - brokerList.size();
                    System.out.println("Server failure: " + broker.getBrokerSocket().getInetAddress() + ": " + broker.getBrokerSocket().getPort()
                            + "\nNumber of servers that have failed: " + serversDown + "\n" + brokerList.size() + " still running" + "\nPlease try again");
                    if (brokerList.isEmpty()) {
                        System.out.println("No more servers are connected to the broker. \nBroker is shutting down...");
                        System.exit(0);
                    }
                    brokenServer = true;
                    break;
                }
            }
            if (!brokenServer) {
                if (invalid) {
                    response = INVALID_REQUEST;
                } else {
                    if (!found && !deleted) {

                        boolean valid = (brokerList.size() - serversToReceiveQuery) >= 0;
                        if (!valid) {
                            System.out.println("WARNING: It seems that there are less than " + serversToReceiveQuery + " servers still running. Can not guarantee the correct output");
                        }
                    }
                    if (!deleted) {
                        response = responseList.get(0);
                    } else {
                        response = memberDeleted;
                    }
                }
                System.out.println(response);
            }

            responseList.clear();
            deleted = false;
            found = false;
            invalid = false;

        } while (!query.equals("exit"));
    }

    private void putDataToServers(List<String> dataList, List<Broker> brokerList, Integer serversToReceiveQuery) {
        dataList.forEach(query -> {
            int randomNumber;
            List<Broker> clonedBrokerList = new ArrayList<>(brokerList);
            for (int i = 0; i < serversToReceiveQuery; i++) {
                int bound = clonedBrokerList.size() - 1;
                randomNumber = random.nextInt(bound);
                try {
                    System.out.println("Sending query: " + query);
                    Broker broker = clonedBrokerList.get(randomNumber);
                    String response = broker.sendMessage(query);
                    System.out.println(BRACKET_PUT + " - Server in port: " + broker.getBrokerSocket().getPort() + " | " + response);
                } catch (IOException exception) {
                    System.out.println(ERROR);
                }
                clonedBrokerList.remove(randomNumber);
                Collections.reverse(clonedBrokerList);
            }
        });
    }

    private List<String> retrieveDataFromFile(String dataToIndex) throws FileNotFoundException {
        List<String> dataToSend = new ArrayList<>();
        try {
            String input;
            String query;
            StringBuilder builder = new StringBuilder(PUT);

            Scanner scanner = new Scanner(new FileReader(dataToIndex));
            while (scanner.hasNextLine()) {
                input = scanner.nextLine();
                query = builder.append(WHITESPACE).append(input).toString();
                dataToSend.add(query);

                query = PUT;
                builder.setLength(0);
                builder.append(query);
            }
        } catch (FileNotFoundException exception) {
            System.out.println("Failed to read file: " + dataToIndex);
            throw new FileNotFoundException();
        }
        return dataToSend;
    }

    private List<Broker> deploy(List<Integer> listOfPorts) {
        List<Broker> brokerList = new ArrayList<>();
        for (int port : listOfPorts) {
            try {
                Broker broker = new Broker();
                broker.startConnection(port);
                brokerList.add(broker);
            } catch (IOException exception) {
                System.out.println("Broker error: Can not connect to server at port: " + port);
            }
        }
        if (brokerList.isEmpty()) {
            System.out.println("Broker could not connect to any server... \n" + "Shutting down broker..");
            System.exit(0);
        }
        return brokerList;
    }


    private boolean validateData(String serverFile, String dataToIndex, Integer serversToReceiveQuery)
            throws IOException {

        if (serverFile == null) {
            System.out.println(SERVER_FILE_ERROR);
        }
        if (dataToIndex == null) {
            throw new IOException(DATA_TO_INDEX_FILE_ERROR);
        }

        if (serversToReceiveQuery == null) {
            return false;
        }
        return true;
    }
}
