package ru.javawebinar.topjava.repository.inmemory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.repository.UserRepository;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private final Map<Integer, Map<Integer, Meal>> repository = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);

    public final static int USER_ID_ONE = 1;
    public final static int USER_ID_TWO = 2;
    public final static int ADMIN_ID = 3;

    {
        save(new Meal(LocalDateTime.of(2020, Month.JANUARY, 29, 10, 0),
                "Завтрак", 500), USER_ID_ONE);
        save(new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0),
                "Обед", 1000), USER_ID_ONE);
        save(new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0),
                "Ужин", 500), USER_ID_ONE);
        save(new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0),
                "Еда на граничное значение", 100), USER_ID_ONE);
        save(new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0),
                "Завтрак", 1000), USER_ID_TWO);
        save(new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0),
                "Обед", 500), ADMIN_ID);
        save(new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0),
                "Ужин", 410), ADMIN_ID);
//        MealsUtil.meals.forEach(meal -> save(meal, 1));
    }

    // null, если обновленная еда не принадлежит userId
    @Override
    public Meal save(Meal meal, int userId) {
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            Map<Integer, Meal> userMeals = repository.get(userId);
            if (userMeals == null) {
                userMeals = new ConcurrentHashMap<>();
            }
            userMeals.put(meal.getId(), meal);
            repository.put(userId, userMeals);
            return meal;
        }
        // handle case: update, but not present in storage
        Map<Integer, Meal> userMeals = userMeals(userId);
        return userMeals == null ? null : userMeals.computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
    }

    // false, если еда не принадлежит userId
    @Override
    public boolean delete(int id, int userId) {
        Map<Integer, Meal> userMeals = userMeals(userId);
        return userMeals != null && userMeals.remove(id) != null;
    }

    // null, если еда не принадлежит userId
    @Override
    public Meal get(int id, int userId) {
        Map<Integer, Meal> userMeals = userMeals(userId);
        return userMeals == null ? null : userMeals.getOrDefault(id, null);
    }

    // ORDERED dateTime desc
    @Override
    public Collection<Meal> getAll() {
        return userMeals(SecurityUtil.authUserId()).values().stream()
                .sorted((m1, m2) -> m2.getDate().compareTo(m1.getDate()))
                .collect(Collectors.toList());
    }

    public Collection<Meal> getAllByUserId(int userId) {
        return userMeals(userId).values().stream()
                .sorted((m1, m2) -> m2.getDate().compareTo(m1.getDate()))
                .collect(Collectors.toList());
    }

    private Map<Integer, Meal> userMeals(int userId) {
        return repository.get(userId);
    }
}

