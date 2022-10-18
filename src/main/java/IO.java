import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
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
}