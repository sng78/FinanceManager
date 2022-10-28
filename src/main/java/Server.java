import org.json.JSONObject;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class Server {
    private static final int PORT = 8989;

    @SuppressWarnings("InfiniteLoopStatement") //убираем ложноверное предупреждение
    public static void main(String[] args) {
        File tsvFile = new File("categories.tsv");
        File dataFile = new File("data.bin");
        List<String[]> listProducts = new ArrayList<>();
        String category;

        //считываем tsv в мапу
        Map<String, String> mapFromFile = IO.readTsv(tsvFile);

        //создаем сеты - для методов Max
        Set<String> categorySet = new HashSet<>(mapFromFile.values());
        categorySet.add("другое");
        Set<String> dateSet = new HashSet<>();

        //загружаем список купленных товаров
        if (dataFile.exists()) {
            listProducts = IO.loadBin(dataFile);
            for (String[] list : listProducts) {
                dateSet.add(list[2]);
            }
        }

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Сервер стартовал");
            while (true) {
                try (Socket socket = serverSocket.accept();
                     BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                     PrintWriter out = new PrintWriter(socket.getOutputStream())
                ) {
                    //получаем и парсим запрос
                    JSONObject json = new JSONObject(in.readLine());
                    String title = (String) json.get("title");
                    String date = (String) json.get("date");
                    String sum = String.valueOf(json.get("sum"));

                    //сопоставляем товар с категорией
                    category = mapFromFile.getOrDefault(title, "другое");

                    //Создаем и заполняем список купленных товаров и возможных дат
                    String[] string = new String[]{title, category, date, sum};
                    listProducts.add(string);
                    dateSet.add(string[2]);

                    //periodCategory (parameter period = 4 - год, 7 - месяц, 10 - день)
                    Map<String, String> maxCategory = Max.category(listProducts, categorySet);
                    Map<String, String> maxYearCategory = Max.periodCategory(listProducts, categorySet, dateSet, 4);
                    Map<String, String> maxMonthCategory = Max.periodCategory(listProducts, categorySet, dateSet, 7);
                    Map<String, String> maxDayCategory = Max.periodCategory(listProducts, categorySet, dateSet, 10);

                    //создаем json и отправляем клиенту
                    JSONObject jsonMax = IO.makeJson(maxCategory, maxYearCategory, maxMonthCategory, maxDayCategory);
                    IO.saveBin(dataFile, listProducts);
                    out.println(jsonMax);
                }
            }
        } catch (IOException e) {
            System.out.println("Не могу стартовать сервер");
            e.printStackTrace();
        }
    }
}