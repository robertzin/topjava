package ru.javawebinar.topjava.repository.datajpa;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class DataJpaMealRepository implements MealRepository {

    private final CrudMealRepository mealRepository;
    private final CrudUserRepository userRepository;

    public DataJpaMealRepository(CrudMealRepository mealRepository, CrudUserRepository userRepository) {
        this.mealRepository = mealRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Meal save(Meal meal, int userId) {
        userRepository.findById(userId).ifPresent(meal::setUser);
        if (meal.isNew()) {
            return mealRepository.save(meal);
        } else if (get(meal.getId(), userId) == null) {
            return null;
        }
        return mealRepository.save(meal);
    }

    @Override
    public boolean delete(int id, int userId) {
        return mealRepository.delete(id, userId) != 0;
    }

    @Override
    public Meal get(int id, int userId) {
        return mealRepository.findById(id)
                .filter(m -> m.getUser().getId() == userId)
                .orElse(null);
    }

    @Override
    public List<Meal> getAll(int userId) {
        return mealRepository.findAll()
                .stream()
                .filter(m -> m.getUser().getId() == userId)
                .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public List<Meal> getBetweenHalfOpen(LocalDateTime startDateTime, LocalDateTime endDateTime, int userId) {
        return mealRepository.findAll()
                .stream()
                .filter(m -> m.getUser().getId() == userId)
                .filter(m -> m.getDateTime().isAfter(startDateTime))
                .filter(m -> m.getDateTime().isBefore(endDateTime))
                .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public Meal getWithUser(int id, int userId) {
        return mealRepository.getWithUser(id, userId);
    }
}
