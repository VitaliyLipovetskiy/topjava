package ru.javawebinar.topjava.service;

import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.Profiles;

/**
 * @author Vitalii Lypovetskyi
 */
@ActiveProfiles(profiles = {Profiles.JPA})
public class UserServiceJpaTest extends UserServiceTest {
}
