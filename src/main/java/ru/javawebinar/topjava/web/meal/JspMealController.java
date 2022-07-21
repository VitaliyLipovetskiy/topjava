package ru.javawebinar.topjava.web.meal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.SecurityUtil;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalDate;
import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalTime;
import static ru.javawebinar.topjava.util.ValidationUtil.assureIdConsistent;

/**
 * @author Vitalii Lypovetskyi
 */
@Controller
@RequestMapping("/meals")
public class JspMealController extends AbstractMealController {

    @GetMapping
    public String getMeals(Model model,
                           HttpServletRequest request) {
        log.info("meals");

//        request.getParameterMap().forEach((par, map) -> {
//            System.out.println(par + "=>" + Arrays.toString(map));
//                }
//        );

        Set<String> filterSet = new HashSet<>(Set.of("startDate", "endDate", "startTime", "endTime"));
        filterSet.retainAll(request.getParameterMap().keySet());
        if (filterSet.isEmpty()) {
            model.addAttribute("meals", MealsUtil.getTos(service.getAll(SecurityUtil.authUserId()), SecurityUtil.authUserCaloriesPerDay()));
        } else {
            LocalDate startDate = parseLocalDate(request.getParameter("startDate"));
            LocalDate endDate = parseLocalDate(request.getParameter("endDate"));
            LocalTime startTime = parseLocalTime(request.getParameter("startTime"));
            LocalTime endTime = parseLocalTime(request.getParameter("endTime"));
            request.setAttribute("meals", getBetween(startDate, startTime, endDate, endTime));
        }
//        switch (action == null ? "all" : action) {
//            case "delete" -> {
//                int id = getId(request);
//                mealController.delete(id);
//                response.sendRedirect("meals");
//            }
//            case "create", "update" -> {
//                final Meal meal = "create".equals(action) ?
//                        new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000) :
//                        mealController.get(getId(request));
//                request.setAttribute("meal", meal);
//                request.getRequestDispatcher("/mealForm.jsp").forward(request, response);
//            }
//        }
        return "meals";
    }

    @GetMapping("/new")
    public String newMeal(@ModelAttribute Meal meal) {
        log.info("newMeal");
//        model.addAttribute("meal", new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000));
        return "mealForm";
    }

    @GetMapping("/{id}")
    public String getMealById(Model model, @PathVariable(value = "id") Integer id,
                              HttpServletRequest request) {
        request.getParameterMap().forEach((par, map) ->
                System.out.println(par + "=>" + Arrays.toString(map)));
        log.info("getMeal {}", id);
        model.addAttribute("meal", service.get(id, SecurityUtil.authUserId()));
        return "mealForm";
    }

    // @RequestParam(value = "key") String key, @RequestBody PaymentRequest request
    @PostMapping
    public String createMeal(HttpServletRequest request) {
        log.info("createMeal");
        service.create(getMeal(request), SecurityUtil.authUserId());
        return "redirect:/meals";
    }

    @PostMapping("/{id}")
    public String updateMeal(@PathVariable(value = "id") Integer id, HttpServletRequest request) {
        log.info("updateMeal id={}", id);
        Meal meal = getMeal(request);
        if (StringUtils.hasLength(request.getParameter("id"))) {
            assureIdConsistent(meal, id);
            service.update(meal, SecurityUtil.authUserId());
        }
        return "redirect:/meals";
    }

    @GetMapping("/delete/{id}")
    public String deleteMeal(@PathVariable(value = "id") Integer id) {
        log.info("deleteMeal id={}", id);
        service.delete(id, SecurityUtil.authUserId());
        return "redirect:/meals";
    }

    private Meal getMeal(HttpServletRequest request) {
        return new Meal(
                LocalDateTime.parse(request.getParameter("dateTime")),
                request.getParameter("description"),
                Integer.parseInt(request.getParameter("calories")));
    }
}
