import java.io.File;
import java.net.Socket;

import io.IOManager;
import io.events.FileReadAdapter;
import io.events.FileReadEvent;
import io.threads.files.FileReadThread;
import net.client.Client;
import packet.Packet;

public class App {
    public static void main(String[] args) throws Exception {
        Socket socket = new Socket("172.30.165.209", 8008);
        Client client = new Client(socket);
        client.start();

        IOManager io = new IOManager(client);

        FileReadThread fileRead = io.readFile(new File("pickleRick.jpg"));
        fileRead.addFileReadListener(new FileReadAdapter() {
            @Override
            public void onFileRead(FileReadEvent event) {
                for (Packet packet : event.getPackets())
                    System.out.println(packet);
                client.send(event.getPackets());
            }
        });
        fileRead.start();

        fileRead = io.readFile(new File("pickleRick.jpg"));
        fileRead.addFileReadListener(new FileReadAdapter() {
            @Override
            public void onFileRead(FileReadEvent event) {
                for (Packet packet : event.getPackets())
                    System.out.println(packet);
                client.send(event.getPackets());
            }
        });
        fileRead.start();
        
        fileRead = io.readFile(new File("pickleRick.jpg"));
        fileRead.addFileReadListener(new FileReadAdapter() {
            @Override
            public void onFileRead(FileReadEvent event) {
                for (Packet packet : event.getPackets())
                    System.out.println(packet);
                client.send(event.getPackets());
            }
        });
        fileRead.start();
    }
}
