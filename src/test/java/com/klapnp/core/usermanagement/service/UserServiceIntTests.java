package com.klapnp.core.usermanagement.service;

import com.klapnp.core.KlapnpCoreApplication;
import com.klapnp.core.usermanagement.model.KlapUser;
import com.klapnp.core.usermanagement.repository.UserRepository;
import com.klapnp.core.usermanagement.service.contract.IUserService;
import com.klapnp.core.usermanagement.service.dto.UserDTO;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = KlapnpCoreApplication.class)
public class UserServiceIntTests {

    private static final String DEFAULT_ACTIVATION_KEY = "AAAAAAAAA";
    private static final String DEFAULT_LOGIN = "LLLLLLLL";
    private static final String DEFAULT_PASSWORD = "PPPPPPPP";
    private static final String DEFAULT_EMAIL = "klapnp@klapnp.com";
    private static final String DEFAULT_FIRSTNAME = "FFFFFFFFF";

    KlapUser user = new KlapUser();

    @TestConfiguration
    static class UserRepositoryTestContextConfiguration {
        @Bean
        public PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
        }
    }

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private IUserService iUserService;


    @Before
    public void setUp() {
        user.setActivationKey(DEFAULT_ACTIVATION_KEY);
        user.setLogin(DEFAULT_LOGIN);
        user.setEmail(DEFAULT_EMAIL);
        user.setPassword(passwordEncoder.encode(DEFAULT_PASSWORD));
    }


    @Test
    public void whenRegisterUser_ReturnUser() {

        //given
        UserDTO userDTO = new UserDTO();
        userDTO.setLogin(DEFAULT_LOGIN);

        userDTO.setEmail(DEFAULT_EMAIL);
        userDTO.setFirstName(DEFAULT_FIRSTNAME);

        //when
        KlapUser registered = iUserService.registerUser(userDTO, DEFAULT_PASSWORD);

        assertThat(registered.getId()).isNotNull();

    }

}
