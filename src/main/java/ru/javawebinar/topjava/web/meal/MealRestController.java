package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static ru.javawebinar.topjava.util.ValidationUtil.assureIdConsistent;
import static ru.javawebinar.topjava.util.ValidationUtil.checkNew;
import static ru.javawebinar.topjava.web.SecurityUtil.authUserId;
import static ru.javawebinar.topjava.web.SecurityUtil.authUserCaloriesPerDay;

@Controller
public class MealRestController {
    protected final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private MealService service;

    public List<MealTo> getAll() {
        log.info("getAll");
        List<MealTo> mealTos = MealsUtil.getTos(service.getAll(), authUserCaloriesPerDay());
        return mealTos;
    }

    public List<MealTo> getAll(Map<String, String> filter) {
        log.info("getAll(filter)");
        LocalTime startTime;
        if (filter.get("startTime") == null) {
            startTime = LocalTime.of(0, 0);
        } else {
            startTime = LocalTime.parse(filter.get("startTime"));
        }
        LocalTime endTime;
        if (filter.get("endTime") == null) {
            endTime = LocalTime.of(23, 59, 59);
        } else {
            endTime = LocalTime.parse(filter.get("endTime"));
        }
        Predicate<Meal> predicate = meal -> true;
        if (filter.get("startDate") != null) {
            predicate = predicate.and(meal -> meal.getDate().plusDays(1).isAfter(LocalDate.parse(filter.get("startDate"))));
        }
        if (filter.get("endDate") != null) {
            predicate = predicate.and(meal -> meal.getDate().minusDays(1).isBefore(LocalDate.parse(filter.get("endDate"))));
        }
        List<MealTo> mealTos = MealsUtil.getFilteredTos(
                service.getAll().stream().filter(predicate).collect(Collectors.toList()),
                authUserCaloriesPerDay(), startTime, endTime);
        return mealTos;
    }

    public Meal get(int id) {
        log.info("get id={} userId={}", id, authUserId());
        return service.get(id, authUserId());
    }

    public Meal create(Meal meal) {
        log.info("create {} userId={}", meal, authUserId());
        checkNew(meal);
        return service.create(meal, authUserId());
    }

    public void delete(int id) {
        log.info("delete id={} userId={}", id, authUserId());
        service.delete(id, authUserId());
    }

    public void update(Meal meal, int id) {
        log.info("update {} with id={} userId={}", meal, id, authUserId());
        assureIdConsistent(meal, id);
        service.update(meal, authUserId());
    }
}