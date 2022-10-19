import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MaxTest {
    Map<String, Integer> mapCosts = new HashMap<>();

    @BeforeEach
    void setUp() {
        mapCosts.put("еда", 50);
        mapCosts.put("одежда", 350);
        mapCosts.put("быт", 0);
    }

    @Test
    @DisplayName("Проверка на верность найденного максимума")
    void clothesTrue() {
        Assertions.assertTrue(Objects.deepEquals(new String[]{"одежда", "350"}, Max.category(mapCosts)));
    }

    @Test
    @DisplayName("Проверка что 1 из 2 осставшихся значений - не максимум")
    void eatFalse() {
        Assertions.assertFalse(Objects.deepEquals(new String[]{"еда", "50"}, Max.category(mapCosts)));
    }

    @Test
    @DisplayName("Проверка что 2 из 2 осставшихся значений - не максимум")
    void modeFalse() {
        Assertions.assertFalse(Objects.deepEquals(new String[]{"быт", "0"}, Max.category(mapCosts)));
    }
}