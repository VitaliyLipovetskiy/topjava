package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
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

    public static final int caloriesPerDay = 2000;
    private static List<Meal> meals;

    public static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=utf-8");
        if (request.getParameterMap().get("action") != null) {
            String parameterAction = request.getParameter("action");
            Meal meal = null;
            String parameterId = request.getParameter("id");
            if (parameterId != null) {
                meal = meals.stream()
                        .filter(m -> m.getId().equals(Integer.parseInt(parameterId)))
                        .findFirst().get();
            }
            switch (parameterAction) {
                case "add":
                    log.debug("redirect to meals add");
//                    request.setAttribute("meal", meal);
                    request.getRequestDispatcher("/meal.jsp").forward(request, response);
                    return;
                case "edit":
                    log.debug("redirect to meals edit");
                    request.setAttribute("meal", meal);
                    request.getRequestDispatcher("/meal.jsp").forward(request, response);
                    return;
                case "delete":
                    log.debug("redirect to meals delete " + meal.getId());
                    meals.remove(meal);
            }
        }
        log.debug("redirect to meals");

        recalculationAndForward(request, response);
        request.getRequestDispatcher("/meals.jsp").forward(request, response);
//        response.sendRedirect("meals.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        //        super.doPost(req, resp);
        log.debug("redirect to Post");

//        req.getParameterMap().keySet().forEach(System.out::println);
//        System.out.println(req.getParameter("id"));
        String parameterId = req.getParameter("id");
//        System.out.println(id);
        if (parameterId.isEmpty()) {
            System.out.println("add");
            meals.add(new Meal(40, LocalDateTime.parse(req.getParameter("dateTime")),
                    req.getParameter("description"),
                    Integer.parseInt(req.getParameter("calories"))));
        } else {
            Meal meal = meals.stream()
                    .filter(m -> m.getId().equals(Integer.parseInt(parameterId)))
                    .findFirst().orElseGet(null);
            System.out.println("update");
            LocalDateTime dateTime = LocalDateTime.parse(req.getParameter("dateTime"));
            if (!meal.getDateTime().equals(dateTime)) {
                meal.setDateTime(dateTime);
            }
            String description = req.getParameter("description");
            System.out.println(description);
            if (!meal.getDescription().equals(description)) {
                meal.setDescription(description);
            }
            int calories = Integer.parseInt(req.getParameter("calories"));
            if (meal.getCalories() != calories) {
                meal.setCalories(calories);
            }
            meals.set(meals.indexOf(meal), meal);
        }

        recalculationAndForward(req, resp);
        resp.sendRedirect("meals");
        ;

    }

    private void recalculationAndForward(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<MealTo> mealsTo = MealsUtil.filteredByStreams(meals, LocalTime.of(0, 0), LocalTime.of(23, 59, 59), caloriesPerDay);
        request.setAttribute("meals", mealsTo);
    }

    @Override
    public void init() throws ServletException {
        super.init();
        meals = new CopyOnWriteArrayList(Arrays.asList(
                new Meal(1, LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new Meal(2, LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new Meal(3, LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new Meal(4, LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new Meal(5, LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new Meal(6, LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new Meal(7, LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
        ));
    }
}
