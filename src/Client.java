import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {

	public static void main(String[] args) throws UnknownHostException, IOException {
		Socket socket = new Socket("localhost", 3304);
		
		PrintWriter pr = new PrintWriter(socket.getOutputStream());
		pr.println("Yes");
		pr.flush();
		
		InputStreamReader in = new InputStreamReader(socket.getInputStream());
		BufferedReader br = new BufferedReader(in);
		
		String str = br.readLine();
		System.out.println("Client: " + str);
	}
}
