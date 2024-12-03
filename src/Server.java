import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {

    private static int port;
    private static ServerSocket serverSocket;
    private static List<ClientHandler> clientHandlers = new ArrayList<>();

    public Server(int port) {
        Server.port = port;
    }

    public static void main(String[] args) {
        if (args.length > 0) {
            port = Integer.parseInt(args[0]);
        } else {
            port = 12345;
        }

        Server server = new Server(port);
        server.startServer();
    }

    public void startServer() {
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Servidor iniciado na porta: " + port);

            while (!serverSocket.isClosed()) {
                Socket socket = serverSocket.accept();
                ClientHandler clientHandler = new ClientHandler(socket, clientHandlers);
                clientHandlers.add(clientHandler);
                new Thread(clientHandler).start();
            }

        } catch (IOException | IllegalArgumentException e) {
            System.err.println("Erro ao inicializar o servidor na porta " + port + ": " + e.getMessage());
        }
    }
}