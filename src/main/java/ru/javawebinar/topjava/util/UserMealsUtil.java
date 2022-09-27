package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.mapper.GetUserMealWithExcess;
import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> meals = Arrays.asList(
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
        );

        List<UserMealWithExcess> mealsTo = filteredByCycles(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        mealsTo.forEach(System.out::println);

        System.out.println(filteredByStreams(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));
    }

    public static List<UserMealWithExcess> filteredByCycles(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        List<UserMealWithExcess> userMealWithExcessList = new ArrayList<>();
        Map<LocalDate, Integer> caloriesPerDayMap = new HashMap<>();

        if (meals != null && startTime != null && endTime != null) {

            for (UserMeal meal : meals) {
                caloriesPerDayMap.merge(ofLocalDate(meal), meal.getCalories(), Integer::sum);
            }

            for (UserMeal meal : meals) {
                if (TimeUtil.isBetweenHalfOpen(ofLocalTime(meal), startTime, endTime)) {
                    userMealWithExcessList.add(GetUserMealWithExcess.toUserMealWithExcess(meal, caloriesPerDayMap.get(ofLocalDate(meal)) > caloriesPerDay));
                }
            }
        }
        return userMealWithExcessList;
    }

    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        if (meals != null && startTime != null && endTime != null) {
            Map<LocalDate, Integer> caloriesPerDayMap = meals.stream().collect(Collectors.toMap(UserMealsUtil::ofLocalDate, UserMeal::getCalories, Integer::sum));
            return meals.stream()
                    .filter(userMeal -> TimeUtil.isBetweenHalfOpen(ofLocalTime(userMeal), startTime, endTime))
                    .map(userMeal -> GetUserMealWithExcess.toUserMealWithExcess(userMeal, caloriesPerDayMap.get(ofLocalDate(userMeal)) > caloriesPerDay))
                    .collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    private static LocalTime ofLocalTime(UserMeal userMeal) {
        return userMeal.getDateTime().toLocalTime();
    }

    private static LocalDate ofLocalDate(UserMeal userMeal) {
        return userMeal.getDateTime().toLocalDate();
    }
}
