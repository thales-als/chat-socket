import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

public class ClientHandler implements Runnable {

    private Socket socket; // Socket do cliente
    private String username; // Nome do usuário
    private PrintWriter out; // Para enviar mensagens ao cliente
    private List<ClientHandler> clientHandlers; // Lista de todos os clientes conectados

    public ClientHandler(Socket socket, List<ClientHandler> clientHandlers) {
        this.socket = socket;
        this.clientHandlers = clientHandlers;
    }

    @Override
    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            // Lê o nome de usuário do cliente
            username = in.readLine();
            System.out.println("Novo usuário conectado: " + username);
            broadcastMessage(username + " se conectou.");

            // Envia mensagem de boas-vindas ao cliente
            out = new PrintWriter(socket.getOutputStream(), true);
            out.println("Bem-vindo, " + username + "!");
            out.println("Para se desconectar, digite: /exit");

            // Processa mensagens recebidas do cliente
            String message;
            while ((message = in.readLine()) != null) {
                broadcastMessage("[" + username + "]: " + message);
            }

        } catch (IOException e) {
            System.err.println("Erro ao lidar com o cliente: " + e.getMessage());
        } finally {
            logout(); // Remove o cliente ao desconectar
        }
    }

    // Envia mensagem para todos os outros clientes
    private void broadcastMessage(String message) {
        for (ClientHandler clientHandler : clientHandlers) {
            if (clientHandler != this) {
                clientHandler.out.println(message);
            }
        }
    }

    // Trata a desconexão do cliente
    private void logout() {
        try {
            broadcastMessage(username + " se desconectou.");
            System.out.println(username + " se desconectou.");
            socket.close();
        } catch (IOException e) {
            System.err.println("Erro ao fechar o socket do cliente: " + e.getMessage());
        }
        clientHandlers.remove(this); // Remove o handler da lista
    }
}
