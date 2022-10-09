package ru.javawebinar.topjava.Storage;

import ru.javawebinar.topjava.model.Meal;

import java.util.List;

public interface MealStorage {
    List<Meal> getAll();

    Meal create(Meal meal);

    Meal update(Meal meal);

    Meal get(int id);

    void delete(int id);
}
