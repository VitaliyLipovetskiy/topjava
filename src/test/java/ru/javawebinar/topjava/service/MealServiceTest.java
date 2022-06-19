package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.*;

/**
 * @author Vitalii Lypovetskyi
 */
@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {

    @Autowired
    private MealService service;

    @Test
    public void get() {
        Meal meal = service.get(100003, USER_ID);
        assertThat(meal).isEqualTo(meal_100003);
    }

    @Test(expected = NotFoundException.class)
    public void getNotFound() throws Exception {
        service.get(1, USER_ID);
    }

    @Test
    public void delete() {
        service.delete(100003, USER_ID);
        service.delete(100004, USER_ID);
        service.delete(100005, USER_ID);
        service.delete(100006, USER_ID);
        service.delete(100007, USER_ID);
        assertThat(service.getAll(USER_ID)).isEqualTo(Arrays.asList(meal_100008, meal_100009));
    }

    @Test(expected = NotFoundException.class)
    public void notFoundDelete() throws Exception {
        service.delete(1, USER_ID);
    }

    @Test
    public void getBetweenInclusive() {
        List<Meal> meals = service.getBetweenInclusive(LocalDate.of(2020, Month.JANUARY, 30),
                LocalDate.of(2020, Month.JANUARY, 30), USER_ID);
        assertThat(meals).isEqualTo(Arrays.asList(meal_100003, meal_100004, meal_100005));
    }

    @Test
    public void getAll() {
        List<Meal> all = service.getAll(USER_ID);
        assertThat(all).isEqualTo(Arrays.asList(meal_100006, meal_100007, meal_100008, meal_100009, meal_100003, meal_100004, meal_100005));
    }

    @Test
    public void update() {
        Meal updated = new Meal(meal_100003);
        updated.setCalories(6000);
        updated.setDescription("Updated");
        service.update(updated, USER_ID);
        assertThat(service.get(100003, USER_ID)).isEqualTo(updated);
    }

    @Test
    public void create() {
        Meal newMeal = new Meal(LocalDateTime.now(), "testing name", 200);
        Meal created = service.create(newMeal, GUEST_ID);
        newMeal.setId(created.getId());
        assertThat(service.getAll(GUEST_ID)).isEqualTo(Arrays.asList(newMeal));
    }

    @Test(expected = DataAccessException.class)
    public void duplicateUserAndDateTimeCreate() throws Exception {
        service.create(new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500), USER_ID);
    }
}