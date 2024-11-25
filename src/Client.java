import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    private static String host = "localhost"; // Endereço do servidor
    private static int port = 12345; // Porta do servidor
    private static boolean isRunning = true; // Controle para encerrar o cliente

    public static void main(String[] args) {
        // Configura o host e a porta a partir dos argumentos ou usa os padrões
        if (args.length > 0) {
            host = args[0];
            port = Integer.parseInt(args[1]);
            System.out.println("Conectando no servidor: " + host);
            System.out.println("Na porta: " + port);
        } else {
            System.out.println("Conectando no servidor padrão: " + host);
            System.out.println("Na porta padrão: " + port);
        }

        try (
            Socket socket = new Socket(host, port); // Conecta ao servidor
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true); // Para enviar mensagens
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream())); // Para receber mensagens
            Scanner sc = new Scanner(System.in) // Para leitura de entrada do usuário
        ) {
            // Lê o nome do usuário e o envia ao servidor
            System.out.print("Digite seu nome de usuário: ");
            String username = sc.nextLine();
            out.println(username);

            // Thread para receber mensagens do servidor
            new Thread(() -> {
                String serverMessage;
                try {
                    while (isRunning && (serverMessage = in.readLine()) != null) {
                        System.out.println(serverMessage);
                    }
                } catch (IOException e) {
                    if (isRunning) {
                        System.err.println("Erro ao ler mensagens do servidor: " + e.getMessage());
                    }
                }
            }).start();

            // Envia mensagens do usuário para o servidor
            String userInput;
            while (isRunning) {
                userInput = sc.nextLine();
                if ("/exit".equalsIgnoreCase(userInput)) {
                    isRunning = false;
                    socket.close(); // Encerra a conexão
                    System.out.println("Encerrando cliente...");
                }
                out.println(userInput);
            }

        } catch (IOException e) {
            System.err.println("Erro ao conectar ao servidor: " + e.getMessage());
        }
    }
}
