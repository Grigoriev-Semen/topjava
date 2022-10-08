package ru.javawebinar.topjava.Storage;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class MealRepository implements StorageForMeal {
    private final Map<Integer, Meal> storageMeal = new ConcurrentHashMap<>();
    private AtomicInteger id = new AtomicInteger(1);

    public MealRepository() {
        for (Meal meal : MealsUtil.getListMealTo()) {
            create(meal);
        }
    }

    @Override
    public List<Meal> getAll() {
        return new ArrayList<>(storageMeal.values());
    }

    @Override
    public Meal create(Meal meal) {
        meal.setId(id.getAndIncrement());
        storageMeal.put(meal.getId(), meal);
        return meal;
    }

    @Override
    public Meal update(Meal meal) {
        if (storageMeal.containsKey(meal.getId())) {
            storageMeal.put(meal.getId(), meal);
        }
        return meal;
    }

    @Override
    public Meal get(int id) {
        return storageMeal.get(id);
    }

    @Override
    public void delete(int id) {
        storageMeal.remove(id);
    }
}
