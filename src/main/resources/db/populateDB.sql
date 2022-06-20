DELETE FROM user_roles;
DELETE FROM users;
DELETE FROM meals;
ALTER SEQUENCE global_seq RESTART WITH 100000;

INSERT INTO users (name, email, password)
VALUES ('User', 'user@yandex.ru', 'password'),
       ('Admin', 'admin@gmail.com', 'admin'),
       ('Guest', 'guest@gmail.com', 'guest');

INSERT INTO user_roles (role, user_id)
VALUES ('USER', 100000),
       ('ADMIN', 100001);

INSERT INTO meals (user_id, date_time, description, calories)
VALUES (100000, '2022-06-17 08:00', 'завтрак', 1100),
       (100000, '2022-06-17 02:00', 'Шпинат', 10),
       (100000, '2022-06-15 23:19', 'Ужин', 1000),
       (100000, '2022-06-15 23:18', 'Ужин', 1000),
       (100000, '2022-06-15 23:17', 'Ужин', 1000),
       (100000, '2022-06-07 16:00', 'обед', 3000),
       (100000, '2022-06-19 11:00', 'завтрак с админом', 750),
       (100001, '2022-06-19 11:00', 'завтрак с юзером', 750);
