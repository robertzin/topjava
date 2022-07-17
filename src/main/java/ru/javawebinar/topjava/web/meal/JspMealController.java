package ru.javawebinar.topjava.web.meal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.javawebinar.topjava.model.Meal;
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
public class JspMealController extends AbstractMealController {

    @GetMapping("/save")
    public String save(Model model) {
        log.info("save meal");
        Meal meal = new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000);
        model.addAttribute("meal", meal);
        return "mealForm";
    }

    @GetMapping("/get")
    public String get(HttpServletRequest request, Model model) {
        log.info("get meal");
        String id = request.getParameter("id");
        Meal meal = service.get(Integer.parseInt(id), SecurityUtil.authUserId());
        model.addAttribute("meal", meal);
        return "mealForm";
    }

    @GetMapping
    public String getAll(Model model) {
        log.info("get all");
        model.addAttribute("meals",
                MealsUtil.getTos(service.getAll(SecurityUtil.authUserId()), SecurityUtil.authUserCaloriesPerDay()));
        return "meals";
    }

    @PostMapping
    public String createUpdate(HttpServletRequest request) {
        final Meal meal = new Meal(LocalDateTime.parse(request.getParameter("dateTime")),
                request.getParameter("description"),
                Integer.parseInt(request.getParameter("calories")));
        String id = request.getParameter("id");
        int userId = SecurityUtil.authUserId();
        if (StringUtils.hasLength(id)) {
            meal.setId(Integer.parseInt(id));
            log.info("update meal");
            service.update(meal, userId);
        } else {
            log.info("create meal");
            service.create(meal, userId);
        }
        return "redirect:/meals";
    }

    @GetMapping("/delete")
    public String delete(HttpServletRequest request) {
        int id = Integer.parseInt(request.getParameter("id"));
        log.info("delete meal {}", id);
        service.delete(id, SecurityUtil.authUserId());
        return "redirect:/meals";
    }

    @GetMapping("/filter")
    public String filter(HttpServletRequest request, Model model) {
        log.info("filter meals");
        LocalDate startDate = parseLocalDate(request.getParameter("startDate"));
        LocalDate endDate = parseLocalDate(request.getParameter("endDate"));
        LocalTime startTime = parseLocalTime(request.getParameter("startTime"));
        LocalTime endTime = parseLocalTime(request.getParameter("endTime"));
        int userId = SecurityUtil.authUserId();
        log.info("get meals between {} and {} from {} to {} for user {}", startDate, endDate, startTime, endDate, userId);
        List<Meal> meals = service.getBetweenInclusive(startDate, endDate, userId);
        List<MealTo> sortedMeals = MealsUtil.getFilteredTos(meals, SecurityUtil.authUserCaloriesPerDay(), startTime, endTime);
        model.addAttribute("meals", sortedMeals);
        return "meals";
    }
}
