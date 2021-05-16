import java.net.Socket;

import net.client.Client;

public class App {
    public static void main(String[] args) throws Exception {        
        Socket socket = new Socket("2.2.2.51", 8008);
        Client client = new Client(socket);
        client.start();
    }
}
