package ru.javawebinar.topjava.Storage;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class InMemoryMealRepository implements MealStorage {
    private final Map<Integer, Meal> storage = new ConcurrentHashMap<>();
    private final AtomicInteger id = new AtomicInteger(1);

    public InMemoryMealRepository() {
        for (Meal meal : MealsUtil.getMealList()) {
            create(meal);
        }
    }

    @Override
    public List<Meal> getAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public Meal create(Meal meal) {
        meal.setId(id.getAndIncrement());
        storage.put(meal.getId(), meal);
        return meal;
    }

    @Override
    public Meal update(Meal meal) {
        storage.replace(meal.getId(), meal);
        return meal;
    }

    @Override
    public Meal get(int id) {
        return storage.get(id);
    }

    @Override
    public void delete(int id) {
        storage.remove(id);
    }
}
