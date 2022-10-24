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
        assertMatch(actual, meal1);
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
                LocalDate.of(2022, Month.OCTOBER, 22), USER_ID), meal3, meal2, meal1);
    }

    @Test
    public void getAll() {
        List<Meal> actual = service.getAll(USER_ID);
        assertMatch(actual, meals);
    }

    @Test
    public void update() {
        service.update(getUpdated(), USER_ID);
        assertMatch(service.get(MEAL_ID, USER_ID), getUpdated());
    }

    @Test
    public void create() {
        Meal actual = service.create(getNew(), USER_ID);
        Integer newId = actual.getId();
        Meal expected = getNew();
        expected.setId(newId);
        assertMatch(actual, expected);
        assertMatch(service.get(newId, USER_ID), expected);
    }

    @Test
    public void createDuplicateDateTime() {
        assertThrows(DuplicateKeyException.class, () ->
                service.create(new Meal(meal1.getDateTime(), "Задублированный ужин", 500), USER_ID));
    }

    @Test
    public void deleteNotFound() {
        assertThrows(NotFoundException.class, () ->
                service.delete(ADMIN_MEAL_ID, USER_ID));
    }

    @Test
    public void updateNotFound() {
        assertThrows(NotFoundException.class, () ->
                service.update(getUpdated(), ADMIN_ID));
        assertMatch(service.get(MEAL_ID, USER_ID), meal1);
    }

    @Test
    public void filterDateTimeNoLimit() {
        assertMatch(service.getBetweenInclusive(null, null, USER_ID),
                meal7, meal6, meal5, meal4, meal3, meal2, meal1);
    }

    @Test
    public void getNotFound() {
        assertThrows(NotFoundException.class, () ->
                assertMatch(service.get(MEAL_ID, ADMIN_ID), meal1));
    }
}