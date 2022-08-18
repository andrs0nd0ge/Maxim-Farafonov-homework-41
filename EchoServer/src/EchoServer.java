import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class EchoServer {
    private final int port;

    private EchoServer(int port) {
        this.port = port;
    }

    public static EchoServer bindToPort(int port) {
        return new EchoServer(port);
    }

    public void run() {
        try (ServerSocket server = new ServerSocket(port)) {
            try (Socket clientSocket = server.accept()) {
                handle(clientSocket);
            }
        } catch (IOException e) {
            System.out.printf("Port %s is probably busy.\n", port);
            e.printStackTrace();
        }
    }

    private void handle(Socket socket) throws IOException {
        InputStream input = socket.getInputStream();
        InputStreamReader isr = new InputStreamReader(input, "UTF-8");

        try (Scanner sc = new Scanner(isr); PrintWriter pw = new PrintWriter(socket.getOutputStream())) {
            while (true) {
                String message = sc.nextLine();
                if ("bye".equalsIgnoreCase(message)) {
                    return;
                }
                pw.write(message);
                pw.write(System.lineSeparator());
                pw.flush();
            }

        } catch (NoSuchElementException e) {
            System.out.println("Client has dropped the connection!");
        }
    }
}
