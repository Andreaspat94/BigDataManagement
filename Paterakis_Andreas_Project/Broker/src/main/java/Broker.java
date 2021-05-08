import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Broker {

    private Socket brokerSocket;
    private PrintWriter out;
    private BufferedReader in;

    public Socket getBrokerSocket() {
        return brokerSocket;
    }

    public void startConnection(int port) throws IOException {
        brokerSocket = new Socket("localhost", port);
        out = new PrintWriter(brokerSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(brokerSocket.getInputStream()));
    }

    public String sendMessage(String msg) throws IOException {
        out.println(msg + "\r\n");
        String response = in.readLine();
        return response;
    }
}
