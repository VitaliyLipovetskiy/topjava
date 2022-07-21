package ru.javawebinar.topjava.service.jdbc;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.service.AbstractUserServiceTest;

import java.util.ArrayList;
import java.util.List;

import static ru.javawebinar.topjava.Profiles.JDBC;
import static ru.javawebinar.topjava.UserTestData.USER_ID;
import static ru.javawebinar.topjava.UserTestData.USER_MATCHER;

@ActiveProfiles(JDBC)
public class JdbcUserServiceTest extends AbstractUserServiceTest {

    @Test
    public void batchUpdate() {
        List<User> updateUsers = new ArrayList<>();
        User created = service.get(USER_ID);
        created.setName("update " + created.getName());
        updateUsers.add(created);
        service.batchUpdate(updateUsers);
        User updateUser = service.get(USER_ID);
        USER_MATCHER.assertMatch(created, updateUser);
    }
}