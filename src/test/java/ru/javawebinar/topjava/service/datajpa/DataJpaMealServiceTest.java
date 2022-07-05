package ru.javawebinar.topjava.service.datajpa;

import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.UserTestData;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.service.AbstractMealServiceTest;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import static org.junit.Assert.assertThrows;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.Profiles.DATAJPA;
import static ru.javawebinar.topjava.UserTestData.USER_MATCHER;

@ActiveProfiles(DATAJPA)
public class DataJpaMealServiceTest extends AbstractMealServiceTest {

    @Test
    public void getWithUser() {
        Meal expectedMeal = MealTestData.meal1;
        User expectedUser = UserTestData.user;
        Meal actualMeal = service.getWithUser(expectedMeal.getId(), expectedUser.getId());
        MEAL_MATCHER.assertMatch(actualMeal, expectedMeal);
        USER_MATCHER.assertMatch(actualMeal.getUser(), expectedUser);
    }

    @Test
    public void getWithUserNotFoundId() {
        assertThrows(NotFoundException.class, () -> service.getWithUser(NOT_FOUND, UserTestData.USER_ID));
    }

    @Test
    public void getWithUserNotOwn() {
        assertThrows(NotFoundException.class, () -> service.getWithUser(MEAL1_ID, UserTestData.NOT_FOUND));
    }
}
