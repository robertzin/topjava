package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertThrows;
import static org.slf4j.LoggerFactory.getLogger;
import static ru.javawebinar.topjava.MealTestData.assertMatch;
import static ru.javawebinar.topjava.MealTestData.getNew;
import static ru.javawebinar.topjava.MealTestData.getUpdated;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.*;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {

    private static final Logger log = getLogger(MealServiceTest.class);

    static {
        // Only for postgres driver logging
        // It uses java.util.logging and logged via jul-to-slf4j bridge
        SLF4JBridgeHandler.install();
    }

    @Autowired
    private MealService service;

    @Test
    public void get() {
        Meal meal = service.get(MEAL_ID_1, USER_ID);
        assertMatch(meal, MEAL_1);
    }

    @Test
    public void getNotFound() {
        assertThrows(NotFoundException.class, () -> service.get(NOT_FOUND, USER_ID));
    }

    @Test
    public void delete() {
        service.delete(MEAL_ID_1, USER_ID);
        assertThrows(NotFoundException.class, () -> service.get(MEAL_ID_1, USER_ID));
    }

    @Test
    public void getBetweenInclusive() {
        assertMatch(service.getBetweenInclusive(LocalDate.of(2022,Month.JUNE, 7), LocalDate.of(2022,Month.JUNE, 15), USER_ID),
                Arrays.asList(MEAL_3, MEAL_4, MEAL_5, MEAL_6));
    }

    @Test
    public void getAll() {
        List<Meal> meals = service.getAll(USER_ID);
        assertMatch(meals, Arrays.asList(MEAL_7, MEAL_1, MEAL_2, MEAL_3, MEAL_4, MEAL_5, MEAL_6));
    }

    @Test
    public void update() {
        Meal updated = getUpdated();
        service.update(updated, USER_ID);
        assertMatch(service.get(MEAL_ID_1, USER_ID), getUpdated());
    }

    @Test
    public void create() {
        Meal created = service.create(getNew(), USER_ID);
        Integer newId = created.getId();
        Meal newMeal = getNew();
        newMeal.setId(newId);
        assertMatch(created, newMeal);
        assertMatch(service.get(newId, USER_ID), newMeal);
    }


    @Test
    public void duplicateDateTimeCreate() {
        assertThrows(DataAccessException.class, () ->
                service.create(new Meal(null, LocalDateTime.of(2022, Month.JUNE, 17,8, 0), "еще завтрак", 910), USER_ID));
    }

    @Test
    public void strangerGet() {
        assertThrows(NotFoundException.class, () -> service.get(MEAL_ID_1, GUEST_ID));
    }

    @Test
    public void strangerUpdate() {
        assertThrows(NotFoundException.class, () -> service.update(getUpdated(), GUEST_ID));
    }

    @Test
    public void strangerDelete() {
        assertThrows(NotFoundException.class, () -> service.delete(MEAL_ID_1, GUEST_ID));
    }
}