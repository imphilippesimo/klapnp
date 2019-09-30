package com.klapnp.core.usermanagement.repository;

import com.klapnp.core.usermanagement.model.KlapUser;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class UserRepositoryTests {

    @Autowired
    private TestEntityManager testEntityManager;

    private static final String DEFAULT_ACTIVATION_KEY = "AAAAAAAAA";
    private static final String DEFAULT_LOGIN = "LLLLLLLL";
    private static final String DEFAULT_PASSWORD = "PPPPPPPP";
    private static final String DEFAULT_EMAIL = "klapnp@klapnp.com";
    private KlapUser user = new KlapUser();

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

    @Before
    public void setUp() {
        //given
        user.setActivationKey(DEFAULT_ACTIVATION_KEY);
        user.setLogin(DEFAULT_LOGIN);
        user.setEmail(DEFAULT_EMAIL);
        user.setPassword(passwordEncoder.encode(DEFAULT_PASSWORD));
        testEntityManager.persist(user);
        testEntityManager.flush();
    }

    @Test
    public void whenFindByActivationKey_thenReturnUser() {
        //when
        Optional<KlapUser> found = userRepository.findOneByActivationKey(DEFAULT_ACTIVATION_KEY);

        //then
        assertThat(found.isPresent()).isTrue();
        assertThat(found.get().getActivationKey()).isEqualTo(DEFAULT_ACTIVATION_KEY);
    }

    @Test
    public void whenFindOneByLogin_theReturnUser() {
        //when
        Optional<KlapUser> found = userRepository.findOneByLogin(DEFAULT_LOGIN);
        //then
        assertThat(found.isPresent()).isTrue();
        assertThat(found.get().getLogin()).isEqualTo(DEFAULT_LOGIN);
    }

    @Test
    public void whenFindOneByEmailIgnoreCase_thenReturnUser() {
        //when
        Optional<KlapUser> found = userRepository.findOneByEmailIgnoreCase(DEFAULT_EMAIL);

        //then
        assertThat(found.isPresent()).isTrue();
        assertThat(found.get().getEmail()).isEqualTo(DEFAULT_EMAIL);
    }

    @Test
    public void whenSaveUser_thenReturnUserWithId() {
        //when
        userRepository.save(user);

        //then
        Optional<KlapUser> found = userRepository.findOneByActivationKey(DEFAULT_ACTIVATION_KEY);
        assertThat(found.isPresent()).isTrue();
        assertThat(found.get().getId()).isNotNull();
        assertThat(found.get().getActivationKey()).isEqualTo(DEFAULT_ACTIVATION_KEY);
    }

    @Test
    public void whenDeleteUser_thenUserIsNotFound() {
        //when
        userRepository.delete(user);

        //then
        Optional<KlapUser> found = userRepository.findOneByActivationKey(DEFAULT_ACTIVATION_KEY);
        assertThat(found.isPresent()).isFalse();
    }


}
