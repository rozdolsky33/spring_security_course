package io.baselogic.springsecurity.service;

import io.baselogic.springsecurity.domain.AppUser;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.validation.ConstraintViolationException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


/**
 * UserContextTests
 *
 * @since chapter1.00
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
class UserContextTests {

    @Autowired
    private UserContext userContext;

    private AppUser owner = new AppUser();


    @BeforeEach
    void beforeEachTest() {
        owner.setId(1);
    }


    @Test
    void initJdbcOperations() {
        assertThat(userContext).isNotNull();
    }


    @Test
    void setCurrentUser() {
        userContext.setCurrentUser(owner);

        AppUser appUser = userContext.getCurrentUser();

        assertThat(appUser).isNotNull();
        assertThat(appUser.getId()).isEqualTo(1);
    }

    @Test
    void setCurrentUser_null_User() {
        assertThrows(ConstraintViolationException.class, () -> {
            userContext.setCurrentUser(null);
        });

    }

    @Test
    void setCurrentUser_invalid_User() {
        assertThrows(IllegalArgumentException.class, () -> {
            userContext.setCurrentUser(new AppUser());
        });
    }

} // The End...
