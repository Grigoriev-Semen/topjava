package ru.javawebinar.topjava.mapper;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

public class GetUserMealWithExcess {

    public static UserMealWithExcess toUserMealWithExcess(UserMeal userMeal, boolean excess){
        return new UserMealWithExcess(userMeal.getDateTime(),
                userMeal.getDescription(),
                userMeal.getCalories(),
                excess);
    }
}
