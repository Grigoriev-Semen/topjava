package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static ru.javawebinar.topjava.util.ValidationUtil.assureIdConsistent;
import static ru.javawebinar.topjava.util.ValidationUtil.checkNew;

@Controller
public class MealRestController {
    protected final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private MealService service;

    public Meal create(Meal meal) {
        log.info("Create meal - {}", meal);
        int userId = SecurityUtil.authUserId();
        checkNew(meal);
        return service.create(userId, meal);
    }

    public Meal update(Meal meal, int id) {
        log.info("Update meal - {} by id - {}", meal, id);
        int userId = SecurityUtil.authUserId();
        assureIdConsistent(meal, id);
        return service.create(userId, meal);
    }

    public void delete(int id) {
        log.info("Delete meal by id - {}", id);
        int userId = SecurityUtil.authUserId();
        service.delete(id, userId);
    }

    public Meal get(int id) {
        log.info("Get meal by id - {}", id);
        int userId = SecurityUtil.authUserId();
        return service.get(id, userId);
    }

    public List<MealTo> getAll() {
        log.info("Get all meals");
        int userId = SecurityUtil.authUserId();
        return service.getAll(userId);
    }

    public List<MealTo> getAllBetweenDate(LocalTime timeStart, LocalTime timeEnd, LocalDate dateStart, LocalDate dateEnd) {
        log.info("Get: timeStart - {}; timeEnd - {}; dateStart - {}; dateEnd - {}", timeStart, timeEnd, dateStart, dateEnd);
        int userId = SecurityUtil.authUserId();
        timeStart = timeStart == null ? LocalTime.MIN : timeStart;
        timeEnd = timeEnd == null ? LocalTime.MAX : timeEnd;
        dateStart = dateStart == null ? LocalDate.MIN : dateStart;
        dateEnd = dateEnd == null ? LocalDate.MAX : dateEnd;
        return service.getAllBetweenDate(userId, LocalDateTime.of(dateStart, timeStart), LocalDateTime.of(dateEnd, timeEnd));
    }
}