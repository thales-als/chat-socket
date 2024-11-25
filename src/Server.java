import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {

    private static int port; // Porta do servidor
    private static ServerSocket serverSocket; // Socket principal do servidor
    private static List<ClientHandler> clientHandlers = new ArrayList<>(); // Lista de clientes conectados

    public Server(int port) {
        Server.port = port;
    }

    public static void main(String[] args) {
        // Define a porta a partir dos argumentos ou usa a porta padrão (12345)
        if (args.length > 0) {
            port = Integer.parseInt(args[0]);
        } else {
            port = 12345;
        }

        // Inicializa o servidor
        Server server = new Server(port);
        server.startServer();
    }

    public void startServer() {
        try {
            serverSocket = new ServerSocket(port); // Cria o socket do servidor
            System.out.println("Servidor iniciado na porta: " + port);

            while (!serverSocket.isClosed()) {
                Socket socket = serverSocket.accept(); // Aguarda conexões de clientes
                ClientHandler clientHandler = new ClientHandler(socket, clientHandlers);
                clientHandlers.add(clientHandler); // Adiciona o cliente à lista de handlers
                new Thread(clientHandler).start(); // Inicia a thread do cliente
            }

        } catch (IOException | IllegalArgumentException e) {
            System.err.println("Erro ao inicializar o servidor na porta " + port + ": " + e.getMessage());
        }
    }
}
