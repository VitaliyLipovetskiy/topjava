package ru.javawebinar.topjava.service;

import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.Profiles;

/**
 * @author Vitalii Lypovetskyi
 */
@ActiveProfiles(profiles = {Profiles.DATAJPA})
public class MealServiceDataJpaTest extends AbstractMealServiceTest {
}
