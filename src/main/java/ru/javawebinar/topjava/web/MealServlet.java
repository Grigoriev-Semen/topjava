package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.Storage.Storage;
import ru.javawebinar.topjava.Storage.StorageMap;
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
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(MealServlet.class);
    private Storage storage;

    @Override
    public void init() throws ServletException {
        super.init();
        storage = new StorageMap();
        for (Meal meal : MealsUtil.getListMealTo()) {
            storage.create(meal);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) {
            List<MealTo> mealsTo = MealsUtil.filteredByStreams(storage.getAll(), LocalTime.MIN, LocalTime.MAX, 2000);
            request.setAttribute("meals", mealsTo);
            request.getRequestDispatcher("/WEB-INF/meal/listMeals.jsp").forward(request, response);
            return;
        }

        Meal meal;
        int id;
        switch (action) {
            case "delete":
                id = Integer.parseInt(request.getParameter("id"));
                log.debug("Delete meal: id - " + id);
                storage.delete(id);
                response.sendRedirect("meals");
                return;
            case "add":
                log.debug("Add meal.");
                meal = new Meal();
                request.setAttribute("meal", meal);
                request.getRequestDispatcher("/WEB-INF/meal/addMeal.jsp").forward(request, response);
                break;
            case "edit":
                id = Integer.parseInt(request.getParameter("id"));
                log.debug("Edit meal :" + id);
                meal = storage.get(id);
                request.setAttribute("meal", meal);
                request.getRequestDispatcher("/WEB-INF/meal/addMeal.jsp").forward(request, response);
                break;
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
        int id = Integer.parseInt(request.getParameter("id"));

        log.debug("Get parametres: id - " + id + "Date - " + ldt + "calories - " + calories + "description - " + description);
        if (id == 0) {
            storage.create(new Meal(ldt, description, calories));
            log.debug("New meal create and add");
        } else {
            storage.update(new Meal(ldt, description, calories, id));
            log.debug("Meal update");
        }
        response.sendRedirect("meals");
    }
}
