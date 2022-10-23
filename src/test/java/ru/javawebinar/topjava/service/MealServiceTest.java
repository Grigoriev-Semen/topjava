package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import static org.junit.Assert.assertThrows;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.ADMIN_ID;
import static ru.javawebinar.topjava.UserTestData.USER_ID;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {

    static {
        // Only for postgres driver logging
        // It uses java.util.logging and logged via jul-to-slf4j bridge
        SLF4JBridgeHandler.install();
    }

    @Autowired
    private MealService service;

    @Test
    public void get() {
        Meal actual = service.get(MEAL_ID, USER_ID);
        assertMatch(actual, meal_1);
    }

    @Test
    public void delete() {
        service.delete(ADMIN_MEAL_ID, ADMIN_ID);
        assertThrows(NotFoundException.class, () -> service.get(ADMIN_MEAL_ID, ADMIN_ID));
    }

    @Test
    public void getBetweenInclusive() {
        assertMatch(service.getBetweenInclusive(
                LocalDate.of(2022, Month.OCTOBER, 22),
                LocalDate.of(2022, Month.OCTOBER, 22), USER_ID), meal_3, meal_2, meal_1);
    }

    @Test
    public void getAll() {
        List<Meal> actual = service.getAll(USER_ID);
        assertMatch(actual, meals);
    }

    @Test
    public void update() {
        Meal expected = getUpdated();
        service.update(expected, USER_ID);
        expected = getUpdated();
        assertMatch(service.get(MEAL_ID, USER_ID), expected);
    }

    @Test
    public void create() {
        Meal expected = getNew();
        Meal actual = service.create(expected, USER_ID);
        expected.setId(actual.getId());
        assertMatch(actual, expected);
        assertMatch(service.get(actual.getId(), USER_ID), expected);
    }

    @Test
    public void createDuplicateDateTime() {
        assertThrows(DuplicateKeyException.class, () ->
                service.create(new Meal(meal_1.getDateTime(), "Задублированный ужин", 500), USER_ID));
    }

    @Test
    public void deleteNotFound() {
        assertThrows(NotFoundException.class, () ->
                service.delete(ADMIN_MEAL_ID, USER_ID));
    }

    @Test
    public void updateNotFound() {
        assertThrows(NotFoundException.class, () ->
                service.update(meal_1, ADMIN_ID));
    }

    @Test
    public void filterDateTimeNoLimit() {
        assertMatch(service.getBetweenInclusive(
                null, null, USER_ID), meal_7, meal_6, meal_5, meal_4, meal_3, meal_2, meal_1);
    }

    @Test
    public void getNotOwnMeal() {
        assertThrows(NotFoundException.class, () ->
                        assertMatch(service.get(MEAL_ID, ADMIN_ID), meal_1));
    }
}