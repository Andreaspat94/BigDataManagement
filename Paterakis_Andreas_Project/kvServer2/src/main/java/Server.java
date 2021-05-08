import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Inet4Address;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private static final Trie trie = new Trie();
    /**
     * Instantiate the server.
     * @param port the port
     */
    public void start(Integer port) {

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server deployed in address: " + Inet4Address.getLocalHost() + " and port: " + serverSocket.getLocalPort());
            String response;
            Socket socket = serverSocket.accept();
            System.out.println("Client Connected. Socket: " + serverSocket.getLocalPort());
            BufferedReader input = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
            PrintWriter output = new PrintWriter(socket.getOutputStream(), true);

            while (true) {
                String lineInput = input.readLine();
                System.out.println("Server received a request ==> " + lineInput);
                if (lineInput.equals("exit")) {
                    break;
                }

                ServerService service = new ServerService();
                response = service.handleRequest(lineInput, trie);
                System.out.println("Server response <== " + response);
                output.println(response);
                input.readLine();
            }

        } catch (
                IOException e) {
            System.out.println("Server exception " + e.getMessage());
        }
    }
}
