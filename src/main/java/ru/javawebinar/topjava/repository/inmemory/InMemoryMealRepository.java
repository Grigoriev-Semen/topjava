package ru.javawebinar.topjava.repository.inmemory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private static final Logger log = LoggerFactory.getLogger(InMemoryMealRepository.class);
    private Map<Integer, Map<Integer, Meal>> repository = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.meals.forEach(meal -> save(meal, 1));
    }

    @Override
    public Meal save(Meal meal, int userId) {
        Map<Integer, Meal> userMeals = repository.getOrDefault(userId, new ConcurrentHashMap<>());

        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            log.info("Save - {} for userId - {}", meal, userId);
        } else {
            log.info("Update {} for userId - {}", meal, userId);
        }
        userMeals.put(meal.getId(), meal);
        repository.put(userId, userMeals);
        return meal;
    }

    @Override
    public boolean delete(int id, int userId) {
        log.info("Delete meal by id - {} for userId - {}", id, userId);
        Map<Integer, Meal> mealMap = repository.getOrDefault(userId, new ConcurrentHashMap<>());
        return mealMap != null && mealMap.remove(id) != null;
    }

    @Override
    public Meal get(int id, int userId) {
        log.info("Get meal by id -  {} for userId - {}", id, userId);
        return repository.getOrDefault(userId, new ConcurrentHashMap<>()).get(id);
    }

    @Override
    public List<Meal> getAll(int userId) {
        log.info("Get all meals by userId {}", userId);
        return repository.getOrDefault(userId, new ConcurrentHashMap<>()).values().stream()
                .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public List<MealTo> getAllBetweenDate(int userId, LocalDateTime start, LocalDateTime end) {
        log.info("Get all between date: LocalDateTime_star - {}; LocalDateTime_end - {}; userId - {}", start, end, userId);
        return new ArrayList<>(MealsUtil.getFilteredTos(getAll(userId), start, end, MealsUtil.DEFAULT_CALORIES_PER_DAY));
    }
}

