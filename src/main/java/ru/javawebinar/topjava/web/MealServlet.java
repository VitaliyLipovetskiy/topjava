package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.web.meal.MealRestController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MealServlet extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(MealServlet.class);

//    private MealRepository repository;

    private MealRestController controller;

    private ClassPathXmlApplicationContext ctx;

    private Map<String, String> filter = new HashMap<>();

    @Override
    public void init() {
        ctx = new ClassPathXmlApplicationContext("./spring/spring-app.xml");
        controller = ctx.getBean(MealRestController.class);
//        repository = new InMemoryMealRepository();
    }

    @Override
    public void destroy() {
        ctx.close();
        super.destroy();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String id = request.getParameter("id");

        Meal meal = new Meal(id.isEmpty() ? null : Integer.valueOf(id),
                LocalDateTime.parse(request.getParameter("dateTime")),
                request.getParameter("description"),
                Integer.parseInt(request.getParameter("calories")));
        log.info(meal.isNew() ? "Create {}" : "Update {}", meal);
        if (meal.isNew()) {
            controller.create(meal);
        } else {
            controller.update(meal, meal.getId());
        }
//        controller.save(meal, InMemoryMealRepository.USER_ID_ONE);
        response.sendRedirect("meals");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        System.out.println("cansel " + request.getParameter("cansel"));
        if (Objects.equals(request.getParameter("cansel"), "true")) {
            filter.clear();
            request.setAttribute("startDate", null);
            request.setAttribute("endDate", null);
            request.setAttribute("startTime", null);
            request.setAttribute("endTime", null);
        } else {
            if (request.getParameter("startDate") != null && !request.getParameter("startDate").isEmpty()) {
                filter.put("startDate", request.getParameter("startDate"));
            }
            if (request.getParameter("endDate") != null && !request.getParameter("endDate").isEmpty()) {
                filter.put("endDate", request.getParameter("endDate"));
            }
            if (request.getParameter("startTime") != null && !request.getParameter("startTime").isEmpty()) {
                filter.put("startTime", request.getParameter("startTime"));
            }
            if (request.getParameter("endTime") != null && !request.getParameter("endTime").isEmpty()) {
                filter.put("endTime", request.getParameter("endTime"));
            }
        }
        switch (action == null ? "all" : action) {
            case "delete":
                int id = getId(request);
                log.info("Delete id={}", id);
                controller.delete(id);
                response.sendRedirect("meals");
                break;
            case "create":
            case "update":
                final Meal meal = "create".equals(action) ?
                        new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000) :
                        controller.get(getId(request));
                request.setAttribute("meal", meal);
                request.getRequestDispatcher("/mealForm.jsp").forward(request, response);
                break;
            case "all":
            default:
                log.info("getAll");
                if (filter.isEmpty()) {
                    request.setAttribute("meals", controller.getAll());
                } else {
                    request.setAttribute("meals", controller.getAll(filter));
                }
                request.setAttribute("filter", filter);
                request.getRequestDispatcher("/meals.jsp").forward(request, response);
                break;
        }
    }

    private int getId(HttpServletRequest request) {
        String paramId = Objects.requireNonNull(request.getParameter("id"));
        return Integer.parseInt(paramId);
    }
}
