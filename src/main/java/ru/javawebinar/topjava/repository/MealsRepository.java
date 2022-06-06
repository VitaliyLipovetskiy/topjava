package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;

import java.util.List;

/**
 * @author Vitalii Lypovetskyi
 */
public interface MealsRepository {
    void add(Meal meal);

    void remove(Meal meal);

    void update(Meal meal);

    List<Meal> getAllMeals();

    Meal getMealById(int mealId);
}
