package ru.javawebinar.topjava;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.web.meal.MealRestController;
import ru.javawebinar.topjava.web.user.AdminRestController;

import java.util.Arrays;

public class SpringMain {
    public static void main(String[] args) {
//        Meal testMeal = new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500);
//        Meal testMealUpdate = new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 1000);
//        testMealUpdate.setId(21);
        // java 7 automatic resource management (ARM)
        try (ConfigurableApplicationContext appCtx = new ClassPathXmlApplicationContext("spring/spring-app.xml")) {
            System.out.println("Bean definition names: " + Arrays.toString(appCtx.getBeanDefinitionNames()));
            AdminRestController adminUserController = appCtx.getBean(AdminRestController.class);
            adminUserController.create(new User(null, "userName", "email@mail.ru", "password", Role.ADMIN));

            MealRestController mealRestController = appCtx.getBean(MealRestController.class);
            MealService mealService = appCtx.getBean(MealService.class);

//            SecurityUtil.setId(1);
//            System.out.println(mealRestController.getAll());
//            SecurityUtil.setId(2);
//            System.out.println(mealRestController.getAll());

//            SecurityUtil.setId(2);
//            System.out.println(mealService.update(testMeal,3));
//            System.out.println(mealRestController.getAll());
//            SecurityUtil.setId(3);
//            System.out.println(mealRestController.getAll());

//            mealService.create(testMeal,1);
//            mealService.update(testMealUpdate,1);
//            System.out.println(mealService.getAll(1));
        }
    }
}
