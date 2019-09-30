package com.klapnp.core.usermanagement.service;

import com.klapnp.core.usermanagement.model.KlapUser;
import com.klapnp.core.usermanagement.repository.UserRepository;
import com.klapnp.core.usermanagement.service.contract.IUserService;
import com.klapnp.core.usermanagement.service.dto.UserDTO;
import com.klapnp.core.usermanagement.service.impl.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
public class UserServiceTests {

    private static final String DEFAULT_ACTIVATION_KEY = "AAAAAAAAA";
    private static final String DEFAULT_LOGIN = "LLLLLLLL";
    private static final String DEFAULT_PASSWORD = "PPPPPPPP";
    private static final String DEFAULT_EMAIL = "klapnp@klapnp.com";
    private static final String DEFAULT_FIRSTNAME = "FFFFFFFFF";

    KlapUser user = new KlapUser();

    @MockBean
    static UserRepository userRepository;

    @Autowired
    static PasswordEncoder passwordEncoder;

    @Autowired
    IUserService iUserService;

    @Before
    public void setUp() {
        user.setActivationKey(DEFAULT_ACTIVATION_KEY);
        user.setLogin(DEFAULT_LOGIN);
        user.setEmail(DEFAULT_EMAIL);
        user.setPassword(passwordEncoder.encode(DEFAULT_PASSWORD));

        Mockito.when(userRepository.findOneByLogin(DEFAULT_LOGIN)).thenReturn(Optional.of(user));
        Mockito.when(userRepository.findOneByEmailIgnoreCase(DEFAULT_EMAIL)).thenReturn(Optional.of(user));
        Mockito.when(userRepository.findOneByActivationKey(DEFAULT_ACTIVATION_KEY)).thenReturn(Optional.of(user));
        Mockito.when(userRepository.save(user)).thenReturn(user);
    }

    @TestConfiguration
    static class UserServiceTestContextConfiguration {

        @Bean
        public IUserService iUserService() {
            return new UserService(userRepository, passwordEncoder);
        }
    }

    @Test
    public void whenRegisterUser_ReturnUser() {

        //given
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail(DEFAULT_EMAIL);
        userDTO.setFirstName(DEFAULT_FIRSTNAME);

        //when
        KlapUser registered = iUserService.registerUser(userDTO, DEFAULT_PASSWORD);

        assertThat(registered.getId()).isNotNull();

    }

}
