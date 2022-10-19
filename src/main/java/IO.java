import org.json.JSONObject;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class IO {
    public static Map<String, String> readTsv(File tsvFile) {
        Map<String, String> map = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(tsvFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\t");
                map.put(parts[0], parts[1]);
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return map;
    }

    public static JSONObject makeJson(Map<String, Integer> mapCosts) {
        String[] maxCategory = Max.category(mapCosts);
        JSONObject jsonMaxInner = new JSONObject();
        jsonMaxInner.put("category", maxCategory[0]);
        jsonMaxInner.put("sum", Integer.parseInt(maxCategory[1]));
        JSONObject jsonMax = new JSONObject();
        jsonMax.put("maxCategory", jsonMaxInner);
        return jsonMax;
    }

    public static void saveBin(File dataFile, Map<String, Integer> mapCosts) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(dataFile))) {
            oos.writeObject(mapCosts);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @SuppressWarnings("unchecked") //убираем предупреждение, тк до этого мы записали мапу, значит ее же и считываем
    public static Map<String, Integer> loadBin(File dataFile) {
        Map<String, Integer> map = null;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(dataFile))) {
            map = (Map<String, Integer>) ois.readObject();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return map;
    }
}