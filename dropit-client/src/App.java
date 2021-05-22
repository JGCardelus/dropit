import java.io.File;
import java.net.Socket;

import io.IOManager;
import io.events.FileReadAdapter;
import io.events.FileReadEvent;
import io.threads.files.FileReadThread;
import net.client.Client;

public class App {
    public static void main(String[] args) throws Exception {        
        Socket socket = new Socket("192.168.8.100", 8008);
        Client client = new Client(socket);
        client.start();

        IOManager io = new IOManager(client);
        for (int i = 0; i < 3; i++) {
            FileReadThread frt = io.readFile(new File("test.pdf"));
            frt.addFileReadListener(new FileReadAdapter(){
                @Override
                public void onFileRead(FileReadEvent event) {
                    client.send(event.getPackets());
                }
            });
            frt.start();
        }

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
