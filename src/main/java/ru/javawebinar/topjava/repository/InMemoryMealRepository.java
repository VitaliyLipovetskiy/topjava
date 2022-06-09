package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Vitalii Lypovetskyi
 */
public class InMemoryMealRepository implements MealRepository {

    private final Map<Integer, Meal> repository = new ConcurrentHashMap<>();
    private final AtomicInteger atomicInteger = new AtomicInteger();

    {
        MealsUtil.meals.forEach(this::save);
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
    public boolean remove(int id) {
        return repository.remove(id) != null;
    }

    @Override
    public Meal save(Meal meal) {
        if (meal.isNew()) {
            meal.setId(getAndIncrement());
            repository.put(meal.getId(), meal);
            return meal;
        }
        return repository.computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
    }

    @Override
    public Collection<Meal> getAll() {
        return repository.values();
    }

    @Override
    public Meal getById(int id) {
        return repository.get(id);
    }
}
