package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.Storage.MealRepository;
import ru.javawebinar.topjava.Storage.StorageForMeal;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(MealServlet.class);
    private StorageForMeal storageForMeal;
    private final int CALORIES_PER_DAY = 2000;

    @Override
    public void init() throws ServletException {
        super.init();
        storageForMeal = new MealRepository();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) {
            List<MealTo> mealsTo = MealsUtil.filteredByStreams(storageForMeal.getAll(), LocalTime.MIN, LocalTime.MAX, CALORIES_PER_DAY);
            request.setAttribute("meals", mealsTo);
            request.getRequestDispatcher("/listMeals.jsp").forward(request, response);
            return;
        }

        switch (action) {
            case "delete": {
                int id = getAndParseId(request);
                log.debug("Delete meal id={} and redirect to meals", id);
                storageForMeal.delete(id);
                response.sendRedirect("meals");
                return;
            }
            case "add": {
                log.debug("Add meal.");
                Meal meal = new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 100);
                request.setAttribute("meal", meal);
                request.getRequestDispatcher("/addMeal.jsp").forward(request, response);
                break;
            }
            case "edit": {
                int id = getAndParseId(request);
                log.debug("Edit meal :" + id);
                Meal meal = storageForMeal.get(id);
                request.setAttribute("meal", meal);
                request.getRequestDispatcher("/addMeal.jsp").forward(request, response);
                break;
            }
        }
        response.sendRedirect("listMeals.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("Create or Update meal");
        request.setCharacterEncoding("UTF-8");

        LocalDateTime ldt = LocalDateTime.parse(request.getParameter("localDateTime"));
        String description = request.getParameter("description");
        int calories = Integer.parseInt(request.getParameter("calories"));
        String id = request.getParameter("id");

        log.debug("Get parameters: id = {}, Date - {}, calories - {}, description - {}", id, ldt, calories, description);

        if (id.isEmpty()) {
            storageForMeal.create(new Meal(ldt, description, calories));
            log.debug("New meal create and add");
        } else {
            storageForMeal.update(new Meal(Integer.parseInt(id), ldt, description, calories));
            log.debug("Meal update");
        }
        response.sendRedirect("meals");
    }

    private int getAndParseId(HttpServletRequest request) {
        return Integer.parseInt(request.getParameter("id"));
    }
}
