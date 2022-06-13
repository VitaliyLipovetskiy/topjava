package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.util.List;

import static ru.javawebinar.topjava.util.ValidationUtil.assureIdConsistent;
import static ru.javawebinar.topjava.util.ValidationUtil.checkNew;

@Controller
public class MealRestController {
    protected final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private MealService service;

    public List<MealTo> getAll() {
        log.info("begin");
        List<MealTo> mealTos = MealsUtil.getTos(service.getAll(), SecurityUtil.authUserCaloriesPerDay());
        log.info("getAll count={}", mealTos.size());
        mealTos.forEach(System.out::println);
        return mealTos;
    }

    public Meal get(int id) {
        log.info("get id={} userId={}", id, SecurityUtil.authUserId());
        return service.get(id, SecurityUtil.authUserId());
    }

    public Meal create(Meal meal) {
        log.info("create {} userId={}", meal, SecurityUtil.authUserId());
        checkNew(meal);
        return service.create(meal, SecurityUtil.authUserId());
    }

    public void delete(int id) {
        log.info("delete id={} userId={}", id, SecurityUtil.authUserId());
        service.delete(id, SecurityUtil.authUserId());
    }

    public void update(Meal meal, int id) {
        log.info("update {} with id={} userId={}", meal, id, SecurityUtil.authUserId());
        assureIdConsistent(meal, id);
        service.update(meal, SecurityUtil.authUserId());
    }
}