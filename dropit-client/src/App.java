import java.io.File;
import java.net.Socket;

import app.users.User;
import io.IOManager;
import io.events.FileReadAdapter;
import io.events.FileReadEvent;
import io.threads.files.FileReadThread;
import net.client.Client;
import packet.types.UserPacket;

public class App {
    public static void main(String[] args) throws Exception {
        Socket socket = new Socket("2.2.2.51", 8008);
        Client client = new Client(socket);
        client.start();

        User user = new User("Another client");
        UserPacket userPacket = new UserPacket(client.nextId());
        userPacket.setUser(user);
        client.send(userPacket);

        // IOManager io = new IOManager(client);
        // for (int i = 0; i < 3; i++) {
        //     FileReadThread frt = io.readFile(new File("test.pdf"));
        //     frt.addFileReadListener(new FileReadAdapter(){
        //         @Override
        //         public void onFileRead(FileReadEvent event) {
        //             client.send(event.getPackets());
        //         }
        //     });
        //     frt.start();
        // }

        // for (int i = 0; i < 1; i++) {
        //     FileReadThread frt = io.readFile(new File("pickleRick.jpg"));
        //     frt.addFileReadListener(new FileReadAdapter(){
        //         @Override
        //         public void onFileRead(FileReadEvent event) {
        //             client.send(event.getPackets());
        //         }
        //     });
        //     frt.start();
        // }
    }
}
