package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.SecurityUtil;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalDate;
import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalTime;

@Controller
@RequestMapping(value = "/meals")
public class JspMealController {
    private static final Logger log = LoggerFactory.getLogger(JspMealController.class);

    @Autowired
    private MealService service;

    @GetMapping("/get")
    public String getMeal(HttpServletRequest request, Model model) {
        Meal meal;
        String id = request.getParameter("id");
        if (!StringUtils.hasLength(id)) {
            meal = new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000);
        } else {
            meal = service.get(Integer.parseInt(id), SecurityUtil.authUserId());
        }
        model.addAttribute("meal", meal);
        return "mealForm";
    }

    @GetMapping
    public String getMeals(Model model) {
        log.info("meals");
        model.addAttribute("meals",
                MealsUtil.getTos(service.getAll(SecurityUtil.authUserId()),
                        SecurityUtil.authUserCaloriesPerDay()));
        return "meals";
    }

    @PostMapping
    public String createUpdateMeal(HttpServletRequest request) {
        log.info("create meal");
        final Meal meal = new Meal(LocalDateTime.parse(request.getParameter("dateTime")),
                request.getParameter("description"),
                Integer.parseInt(request.getParameter("calories")));
        String id = request.getParameter("id");
        int userId = SecurityUtil.authUserId();
        if (StringUtils.hasLength(id)) {
            meal.setId(Integer.parseInt(id));
            service.update(meal, userId);
        } else {
            service.create(meal, userId);
        }
        return "redirect:/meals";
    }

    @GetMapping("/delete")
    public String deleteMeal(HttpServletRequest request) {
        int id = Integer.parseInt(request.getParameter("id"));
        log.info("delete meal {}", id);
        service.delete(id, SecurityUtil.authUserId());
        return "redirect:/meals";
    }

    @GetMapping("/filter")
    public String filterMeals(HttpServletRequest request, Model model) {
        log.info("filter meals");
        LocalDate startDate = parseLocalDate(request.getParameter("startDate"));
        LocalDate endDate = parseLocalDate(request.getParameter("endDate"));
        LocalTime startTime = parseLocalTime(request.getParameter("startTime"));
        LocalTime endTime = parseLocalTime(request.getParameter("endTime"));
        int userId = SecurityUtil.authUserId();
        log.info("get meals between {} and {} from {} to {} for user {}", startDate, endDate, startTime, endDate, userId);
        List<Meal> unsortedMeals = service.getBetweenInclusive(startDate, endDate, userId);
        List<MealTo> meals = MealsUtil.getFilteredTos(unsortedMeals, SecurityUtil.authUserCaloriesPerDay(), startTime, endTime);
        model.addAttribute("meals", meals);
        return "meals";
    }
}
