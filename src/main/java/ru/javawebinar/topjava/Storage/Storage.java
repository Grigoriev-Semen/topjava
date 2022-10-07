package ru.javawebinar.topjava.Storage;

import ru.javawebinar.topjava.model.Meal;

import java.util.List;

public interface Storage {
    List<Meal> getAll();

    void create(Meal meal);

    void update(Meal meal);

    Meal get(Integer id);

    void delete(Integer id);
}
