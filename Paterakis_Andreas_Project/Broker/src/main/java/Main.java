import java.io.IOException;

public class Main {

    /**
     *      kvBroker -s serverFile.txt -i dataToIndex.txt -k 2
     *
     *      -s: number of server addresses
     *      -i: the generated data
     *      -k: the replication factor. i.e how many servers will have the same replicated data
     */

    public static void main(String[] args) throws IOException {

        String s = null; String i = null; Integer k = null;

        //TODO: replace 'array' with 'args'
        for (int j = 0; j < args.length; j++) {
            if (args[j].equals("-k")) {
                k = Integer.parseInt(args[j+1]);
            }
            if (args[j].equals("-i")) {
                i = args[j+1];
            }
            if (args[j].equals("-s")) {
                s = args[j+1];
            }
        }

        BrokerService brokerService = new BrokerService();
        brokerService.constructBroker(s, i, k);


    }
}
