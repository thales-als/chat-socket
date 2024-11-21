import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

	public static void main(String[] args) throws IOException {
		ServerSocket serverSocket = new ServerSocket(3304);
		Socket socket = serverSocket.accept();
		
		System.out.println("Client connected.");
		
		InputStreamReader in = new InputStreamReader(socket.getInputStream());
		BufferedReader br = new BufferedReader(in);
		
		String str = br.readLine();
		System.out.println("Server: " + str);
		
		PrintWriter pr = new PrintWriter(socket.getOutputStream());
		pr.println("Is it working?");
		pr.flush();
	}
}
