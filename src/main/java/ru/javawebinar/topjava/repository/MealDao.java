package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Vitalii Lypovetskyi
 */
public class MealDao implements MealsRepository {

    private static CopyOnWriteArrayList<Meal> meals = new CopyOnWriteArrayList<>();
    private final static AtomicInteger atomicInteger = new AtomicInteger();

    public MealDao() {
        add(new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500));
        add(new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000));
        add(new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500));
        add(new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100));
        add(new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000));
        add(new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500));
        add(new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410));
    }

    private final int getAndIncrement() {
        for (; ; ) {
            int current = atomicInteger.get();
            int next = (current + 1) % Integer.MAX_VALUE;
            if (atomicInteger.compareAndSet(current, next))
                return current;
        }
    }

    @Override
    public void add(Meal meal) {
        meal.setId(getAndIncrement());
        meals.add(meal);
    }

    @Override
    public void remove(Meal meal) {
        meals.remove(meal);
    }

    @Override
    public void update(Meal meal) {
        meals.set(meal.getId(), meal);
    }

    @Override
    public List<Meal> getAllMeals() {
        return meals;
    }

    @Override
    public Meal getMealById(int mealId) {
        return meals.stream().filter(m -> m.getId().equals(mealId)).findFirst().get();
    }
}
