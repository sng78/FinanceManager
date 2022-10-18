import java.util.Map;

public class Max {
    public static String[] category(Map<String, Integer> mapCosts) {
        int maxSum = 0;
        String maxCategory = null;
        for (Map.Entry<String, Integer> entry : mapCosts.entrySet())
            if (entry.getValue() > maxSum) {
                maxSum = entry.getValue();
                maxCategory = entry.getKey();
            }
        return new String[]{maxCategory, String.valueOf(maxSum)};
    }
}