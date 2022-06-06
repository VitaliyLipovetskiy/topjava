package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.repository.MealDao;
import ru.javawebinar.topjava.repository.MealsRepository;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(MealServlet.class);

    private final MealsRepository repository;

    public static final int caloriesPerDay = 2000;

    public static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public MealServlet() {
        this.repository = new MealDao();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=utf-8");
        if (request.getParameterMap().get("action") != null) {
            String parameterAction = request.getParameter("action");
            if (parameterAction.equals("add")) {
                log.debug("redirect to meals add");
                request.getRequestDispatcher("/meal.jsp").forward(request, response);
                return;
            }
            Integer id = Integer.parseInt(request.getParameter("id"));
            Meal meal = repository.getMealById(id);

            switch (parameterAction) {
                case "edit":
                    log.debug("redirect to meals edit");
                    request.setAttribute("meal", meal);
                    request.getRequestDispatcher("/meal.jsp").forward(request, response);
                    return;
                case "delete":
                    log.debug("redirect to meals delete " + meal.getId());
                    repository.remove(meal);
                    List<MealTo> mealsTo = MealsUtil.filteredByStreams(repository.getAllMeals(), LocalTime.of(0, 0), LocalTime.of(23, 59, 59), caloriesPerDay);
                    request.setAttribute("meals", mealsTo);
                    response.sendRedirect("meals");
                    return;
            }
        }
        log.debug("redirect to meals");

        List<MealTo> mealsTo = MealsUtil.filteredByStreams(repository.getAllMeals(), LocalTime.of(0, 0), LocalTime.of(23, 59, 59), caloriesPerDay);
        request.setAttribute("meals", mealsTo);
        request.getRequestDispatcher("/meals.jsp").forward(request, response);

//        response.sendRedirect("meals.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        log.debug("redirect to Post");

        String parameterId = req.getParameter("id");
        if (parameterId == null || parameterId.isEmpty()) {
            repository.add(new Meal(LocalDateTime.parse(req.getParameter("dateTime")),
                    req.getParameter("description"),
                    Integer.parseInt(req.getParameter("calories").isEmpty() ? "0" : req.getParameter("calories"))));
        } else {
            Meal meal = repository.getMealById(Integer.parseInt(parameterId));
            LocalDateTime dateTime = LocalDateTime.parse(req.getParameter("dateTime"));
            if (!meal.getDateTime().equals(dateTime)) {
                meal.setDateTime(dateTime);
            }
            String description = req.getParameter("description");
            if (!meal.getDescription().equals(description)) {
                meal.setDescription(description);
            }
            int calories = Integer.parseInt(req.getParameter("calories"));
            if (meal.getCalories() != calories) {
                meal.setCalories(calories);
            }
            repository.update(meal);
        }

        List<MealTo> mealsTo = MealsUtil.filteredByStreams(repository.getAllMeals(), LocalTime.of(0, 0), LocalTime.of(23, 59, 59), caloriesPerDay);
        req.setAttribute("meals", mealsTo);
        resp.sendRedirect("meals");

    }

}
