import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

public class ClientHandler implements Runnable {

    private Socket socket;
    private String username;
    private PrintWriter out;
    private List<ClientHandler> clientHandlers;

    public ClientHandler(Socket socket, List<ClientHandler> clientHandlers) {
        this.socket = socket;
        this.clientHandlers = clientHandlers;
    }

    @Override
    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            username = in.readLine();
            System.out.println("Novo usuário conectado: " + username);
            broadcastMessage(username + " se conectou.");

            out = new PrintWriter(socket.getOutputStream(), true);
            out.println("Bem-vindo, " + username + "!");
            out.println("Para se desconectar, digite: /quit");

            String message;
            while ((message = in.readLine()) != null) {
                broadcastMessage("[" + username + "]: " + message);
            }

        } catch (IOException e) {
            System.err.println("Erro ao lidar com o cliente: " + e.getMessage());
        } finally {
            logout();
        }
    }

    private void broadcastMessage(String message) {
        for (ClientHandler clientHandler : clientHandlers) {
            if (clientHandler != this) {
                clientHandler.out.println(message);
            }
        }
    }

    private void logout() {
        try {
            broadcastMessage(username + " se desconectou.");
            System.out.println(username + " se desconectou.");
            socket.close();
        } catch (IOException e) {
            System.err.println("Erro ao fechar o socket do cliente: " + e.getMessage());
        }
        clientHandlers.remove(this);
    }
}