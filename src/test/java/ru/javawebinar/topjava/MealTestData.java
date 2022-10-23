package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

public class MealTestData {
    public static final int MEAL_ID = START_SEQ + 3;
    public static final int ADMIN_MEAL_ID = START_SEQ + 10;

    public static final Meal meal_1 = new Meal(MEAL_ID, LocalDateTime.of(2022, Month.OCTOBER, 22, 10, 0), "Завтрак", 500);
    public static final Meal meal_2 = new Meal(MEAL_ID + 1, LocalDateTime.of(2022, Month.OCTOBER, 22, 13, 0), "Обед", 1000);
    public static final Meal meal_3 = new Meal(MEAL_ID + 2, LocalDateTime.of(2022, Month.OCTOBER, 22, 20, 0), "Ужин", 500);
    public static final Meal meal_4 = new Meal(MEAL_ID + 3, LocalDateTime.of(2022, Month.OCTOBER, 23, 0, 0), "Еда на граничное значение", 100);
    public static final Meal meal_5 = new Meal(MEAL_ID + 4, LocalDateTime.of(2022, Month.OCTOBER, 23, 10, 0), "Завтрак", 1000);
    public static final Meal meal_6 = new Meal(MEAL_ID + 5, LocalDateTime.of(2022, Month.OCTOBER, 23, 13, 0), "Обед", 500);
    public static final Meal meal_7 = new Meal(MEAL_ID + 6, LocalDateTime.of(2022, Month.OCTOBER, 23, 20, 0), "Ужин", 410);
    public static final Meal adminMeal_1 = new Meal(ADMIN_MEAL_ID, LocalDateTime.of(2022, Month.OCTOBER, 21, 10, 0), "Админ завтрак", 500);
    public static final Meal adminMeal_2 = new Meal(ADMIN_MEAL_ID + 1, LocalDateTime.of(2022, Month.OCTOBER, 21, 20, 0), "Админ ужин", 1500);

    public static final List<Meal> meals = Arrays.asList(meal_7, meal_6, meal_5, meal_4, meal_3, meal_2, meal_1);

    public static Meal getNew() {
        return new Meal(null, LocalDateTime.of(2022, Month.OCTOBER, 22, 10, 10), "Новый завтрак", 500);
    }

    public static Meal getUpdated() {
        return new Meal(MEAL_ID, LocalDateTime.of(2022, Month.OCTOBER, 22, 10, 0), "Новый завтрак", 800);
    }

    public static void assertMatch(Meal actual, Meal expected) {
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    public static void assertMatch(Iterable<Meal> actual, Meal... expected) {
        assertMatch(actual, Arrays.asList(expected));
    }

    public static void assertMatch(Iterable<Meal> actual, Iterable<Meal> expected) {
        assertThat(actual).usingRecursiveFieldByFieldElementComparator().isEqualTo(expected);
    }
}
