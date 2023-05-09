import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        try (Socket clientSocket = new Socket("localhost", 8989);
             PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);
             BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {
            System.out.println("Введите слово");
            String input = scanner.nextLine();
            writer.println(input);
            Gson gson = new Gson();
            Type founderListType = new TypeToken<ArrayList<PageEntry>>() {}.getType();

            List<PageEntry> result = gson.fromJson(reader.readLine(), founderListType);

            System.out.println(result);

        }
    }
}
