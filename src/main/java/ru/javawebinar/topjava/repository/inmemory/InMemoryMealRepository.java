package ru.javawebinar.topjava.repository.inmemory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private static final Logger log = LoggerFactory.getLogger(InMemoryMealRepository.class);
    private final Map<Integer, Map<Integer, Meal>> repository = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.userMeals.forEach(meal -> save(meal, 1));
        MealsUtil.adminMeals.forEach(meal -> save(meal, 2));
    }

    @Override
    public Meal save(Meal meal, int userId) {
        Map<Integer, Meal> mealMap = repository.computeIfAbsent(userId, id -> new ConcurrentHashMap<>());
        if (meal.isNew()) {
            log.info("Save - {} for userId - {}", meal, userId);
            meal.setId(counter.incrementAndGet());
            mealMap.put(meal.getId(), meal);
        } else {
            log.info("Update {} for userId - {}", meal, userId);
            mealMap.computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
        }
        repository.put(userId, mealMap);
        return meal;
    }

    @Override
    public boolean delete(int id, int userId) {
        log.info("Delete meal by id - {} for userId - {}", id, userId);
        Map<Integer, Meal> mealMap = repository.get(userId);
        return mealMap != null && mealMap.remove(id) != null;
    }

    @Override
    public Meal get(int id, int userId) {
        log.info("Get meal by id -  {} for userId - {}", id, userId);
        Map<Integer, Meal> mealMap = repository.get(userId);
        return mealMap == null ? null : mealMap.get(id);
    }

    @Override
    public List<Meal> getAll(int userId) {
        log.info("Get all meals by userId {}", userId);
        Map<Integer, Meal> mealMap = repository.get(userId);
        return mealMap != null ? doSorted(mealMap.values(), meal -> true) : Collections.emptyList();
    }

    @Override
    public List<Meal> getAllBetweenDate(LocalDate start, LocalDate end, int userId) {
        log.info("Get all between date: LocalDate_start - {}; LocalDate_end - {}; userId - {}", start, end, userId);
        Map<Integer, Meal> mealMap = repository.get(userId);

        if (mealMap != null) {
            return doSorted(mealMap.values(), meal -> DateTimeUtil.isBetween(meal.getDateTime(), start.atStartOfDay(), end.atTime(LocalTime.MAX)));
        } else {
            return Collections.emptyList();
        }
    }

    private List<Meal> doSorted(Collection<Meal> meals, Predicate<Meal> filter) {
        return meals.stream()
                .filter(filter)
                .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                .collect(Collectors.toList());
    }
}

