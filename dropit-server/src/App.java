import io.IOManager;
import net.server.Server;
import packet.Packet;

public class App {
    public static void main(String[] args) throws Exception {
        Server server = new Server();
        server.start();

        IOManager ioManager = new IOManager(server);
    }
}
