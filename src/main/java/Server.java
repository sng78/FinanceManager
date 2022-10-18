import org.json.JSONObject;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.stream.Collectors;

public class Server {
    private static final int PORT = 8989;

    @SuppressWarnings("InfiniteLoopStatement") //убираем ложноверное предупреждение
    public static void main(String[] args) {
        File tsvFile = new File("categories.tsv");

        //считываем tsv в мапу
        Map<String, String> mapFromFile = IO.readTsv(tsvFile);

        //создаем мапу из уникальных категорий
        Set<String> valuesSet = new HashSet<>(mapFromFile.values());
        Map<String, Integer> mapCosts = valuesSet.stream()
                .collect(Collectors.toMap(x -> x, y -> 0));
        mapCosts.put("другое", 0);

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Сервер стартовал");
            while (true) {
                try (Socket socket = serverSocket.accept();
                     BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                     PrintWriter out = new PrintWriter(socket.getOutputStream())
                ) {
                    //получаем и парсим запрос
                    JSONObject js = new JSONObject(in.readLine());
                    String title = (String) js.get("title");
                    int sum = (int) js.get("sum");

                    //заполняем мапу категорий стоимостью
                    if (mapFromFile.containsKey(title)) {
                        int oldSum = mapCosts.get(mapFromFile.get(title));
                        mapCosts.replace(mapFromFile.get(title), (oldSum + sum));
                    } else {
                        int oldSum = mapCosts.get("другое");
                        mapCosts.replace("другое", (oldSum + sum));
                    }

                    //создаем json и отправляем клиенту
                    JSONObject jsonMax = IO.makeJson(mapCosts);
                    out.println(jsonMax);
                }
            }
        } catch (IOException e) {
            System.out.println("Не могу стартовать сервер");
            e.printStackTrace();
        }
    }
}