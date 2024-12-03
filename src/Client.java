import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    private static String host = "localhost";
    private static int port = 12345;
    private static boolean isRunning = true;

    public static void main(String[] args) {
    	configureServerHostAndPort(args);

        try (
            Socket socket = new Socket(host, port);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            Scanner sc = new Scanner(System.in)
        ) {
        	
        	getUsername(sc, out);
        	listenForMessages(in);

            String userInput;
            
            while (isRunning) {
                userInput = sc.nextLine();
                if ("/quit".equalsIgnoreCase(userInput)) {
                    isRunning = false;
                    socket.close();
                    System.out.println("Encerrando cliente...");
                }
                out.println(userInput);
            }

        } catch (IOException e) {
            System.err.println("Erro ao conectar ao servidor: " + e.getMessage());
        }
    }
    
    public static void configureServerHostAndPort(String[] args) {
    	if (args.length > 0) {
            host = args[0];
            port = Integer.parseInt(args[1]);
            System.out.println("Conectando no servidor: " + host);
            System.out.println("Na porta: " + port);
        } else {
            System.out.println("Conectando no servidor padrão: " + host);
            System.out.println("Na porta padrão: " + port);
        }
    }
    
    public static void getUsername(Scanner sc, PrintWriter out) {
    	System.out.print("Digite seu nome de usuário: ");
    	String username = sc.nextLine();
    	out.println(username);
    }
    
    public static void listenForMessages(BufferedReader in) {
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
    }
}