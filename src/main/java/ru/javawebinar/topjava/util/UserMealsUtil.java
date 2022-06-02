package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;

import static java.util.stream.Collectors.*;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> meals = Arrays.asList(
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
        );

        List<UserMealWithExcess> mealsTo = filteredByCycles(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        mealsTo.forEach(System.out::println);

        System.out.println(filteredByStreams(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));
    }

    public static List<UserMealWithExcess> filteredByCycles(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        // Придумывай переменным информативные имена, как можно ближе отражающие их смысл.
        // Тут суммы калорий по дням.
        Map<LocalDate, Integer> totalCaloriesByDay = new HashMap<>();
        meals.forEach(user -> totalCaloriesByDay.merge(user.getDate(), user.getCalories(), Integer::sum));
        // объявляй локальные переменные как можно ближе к месту использования, тем самым минимизируя область видимости.
        List<UserMealWithExcess> result = new ArrayList<>();
        meals.forEach(user -> {
            if (TimeUtil.isBetweenHalfOpen(user.getTime(), startTime, endTime)) {
                result.add(new UserMealWithExcess(user.getDateTime(), user.getDescription(), user.getCalories(),
                        totalCaloriesByDay.get(user.getDate()) > caloriesPerDay));
            }
        });
        return result;
    }

    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> totalCaloriesByDay =
                meals.stream().collect(groupingBy(UserMeal::getDate, summingInt(UserMeal::getCalories)));
//        meals.stream().collect(toMap(UserMeal::getDate, UserMeal::getCalories, Integer::sum));
        return meals.stream().filter(user -> TimeUtil.isBetweenHalfOpen(user.getTime(), startTime, endTime))
                .map(user -> new UserMealWithExcess(user.getDateTime(), user.getDescription(), user.getCalories(),
                        totalCaloriesByDay.get(user.getDate()) > caloriesPerDay))
                .collect(toList());
    }

}
