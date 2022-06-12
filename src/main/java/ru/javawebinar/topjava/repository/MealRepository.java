package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;

import java.util.Collection;

// TODO add userId
public interface MealRepository {
    // null if updated meal does not belong to userId
    // null, если обновленная еда не принадлежит userId
    Meal save(Meal meal, int userId);

    // false if meal does not belong to userId
    // false, если еда не принадлежит userId
    boolean delete(int id, int userId);

    // null if meal does not belong to userId
    // null, если еда не принадлежит userId
    Meal get(int id, int userId);

    // ORDERED dateTime desc
    Collection<Meal> getAll();
}
