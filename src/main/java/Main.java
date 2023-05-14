import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) throws Exception {
        BooleanSearchEngine engine = new BooleanSearchEngine(new File("pdfs"));
        try (ServerSocket serverSocket = new ServerSocket(8989);) {
            while (true) {
                try (Socket socket = serverSocket.accept();
                     BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                     PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

                    System.out.println("New connection accepted");
                    System.out.println("Подключен клиент " + socket.getPort());
                    String request = in.readLine();

                    try (PrintWriter writer = new PrintWriter("response.json")) {
                        Gson gson = new GsonBuilder().create();
                        String clientrequestJson = gson.toJson(engine.search(request));
                        writer.println(clientrequestJson);
                        out.println(clientrequestJson);

                    }

                }
            }
        }
    }
}