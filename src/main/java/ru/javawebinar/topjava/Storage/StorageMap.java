package ru.javawebinar.topjava.Storage;

import ru.javawebinar.topjava.model.Meal;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class StorageMap implements Storage {
    private final Map<Integer, Meal> mealToList;
    private static AtomicInteger id = new AtomicInteger(1);

    public StorageMap() {
        this.mealToList = new ConcurrentHashMap<>();
    }

    @Override
    public List<Meal> getAll() {
        return new ArrayList<>(mealToList.values());
    }

    @Override
    public void create(Meal meal) {
        meal.setId(id.getAndIncrement());
        mealToList.put(meal.getId(), meal);
    }

    @Override
    public void update(Meal meal) {
        mealToList.put(meal.getId(), meal);
    }

    @Override
    public Meal get(Integer id) {
        return mealToList.get(id);
    }

    @Override
    public void delete(Integer id) {
        mealToList.remove(id);

    }
}
