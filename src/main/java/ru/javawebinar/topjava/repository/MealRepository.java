package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;

import java.util.Collection;

/**
 * @author Vitalii Lypovetskyi
 */
public interface MealRepository {
    Meal save(Meal meal);

    boolean remove(int id);

    Collection<Meal> getAll();

    Meal getById(int id);
}
