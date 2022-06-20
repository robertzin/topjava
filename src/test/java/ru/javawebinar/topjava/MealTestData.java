package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

public class MealTestData {

    public static final int MEAL_ID_1 = START_SEQ + 3;
    public static final int MEAL_ID_2 = START_SEQ + 4;
    public static final int MEAL_ID_3 = START_SEQ + 5;
    public static final int MEAL_ID_4 = START_SEQ + 6;
    public static final int MEAL_ID_5 = START_SEQ + 7;
    public static final int MEAL_ID_6 = START_SEQ + 8;
    public static final int MEAL_ID_7 = START_SEQ + 9;
    public static final int MEAL_ID_8 = START_SEQ + 10;

    public static final Meal userMealOne = new Meal(MEAL_ID_1, LocalDateTime.of(2022, Month.JUNE, 17, 8, 0), "завтрак", 1100);
    public static final Meal userMealTwo = new Meal(MEAL_ID_2, LocalDateTime.of(2022, Month.JUNE, 17, 2, 0), "Шпинат", 10);
    public static final Meal userMealThree = new Meal(MEAL_ID_3, LocalDateTime.of(2022, Month.JUNE, 15, 23, 19), "Ужин", 1000);
    public static final Meal userMealFour = new Meal(MEAL_ID_4, LocalDateTime.of(2022, Month.JUNE, 15, 23, 18), "Ужин", 1000);
    public static final Meal userMealFive = new Meal(MEAL_ID_5, LocalDateTime.of(2022, Month.JUNE, 15, 23, 17), "Ужин", 1000);
    public static final Meal userMealSix = new Meal(MEAL_ID_6, LocalDateTime.of(2022, Month.JUNE, 7, 16, 0), "обед", 3000);
    public static final Meal userMealSeven = new Meal(MEAL_ID_7, LocalDateTime.of(2022, Month.JUNE, 19, 11, 0), "завтрак с админом", 750);
    public static final Meal adminMealEight = new Meal(MEAL_ID_8, LocalDateTime.of(2022, Month.JUNE, 19, 11, 0), "завтрак с юзером", 750);

    public static Meal getNew() {
        return new Meal(null, LocalDateTime.of(2022, Month.JUNE, 20, 9, 30), "new завтрак", 1300);
    }

    public static Meal getUpdated() {
        Meal updated = new Meal(userMealOne);
        updated.setDateTime(LocalDateTime.of(2022, Month.JUNE, 17, 10, 0));
        updated.setDescription("updated завтрак");
        updated.setCalories(1500);
        return updated;
    }

    public static void assertMatch(Meal actual, Meal expected) {
        assertThat(actual).usingRecursiveComparison().ignoringFields("user_id", "date_time").isEqualTo(expected);
    }

    public static void assertMatch(Iterable<Meal> actual, Meal... expected) {
        assertMatch(actual, Arrays.asList(expected));
    }

    public static void assertMatch(Iterable<Meal> actual, Iterable<Meal> expected) {
        assertThat(actual).usingRecursiveFieldByFieldElementComparatorIgnoringFields("user_id", "date_time").isEqualTo(expected);
    }
}
