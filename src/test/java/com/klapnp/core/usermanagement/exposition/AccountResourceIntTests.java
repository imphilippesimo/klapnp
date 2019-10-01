package com.klapnp.core.usermanagement.exposition;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.klapnp.core.KlapnpCoreApplication;
import com.klapnp.core.usermanagement.exposition.vm.RegisterUserVM;
import com.klapnp.core.usermanagement.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = KlapnpCoreApplication.class)
@AutoConfigureMockMvc
public class AccountResourceIntTests {
    private static final String DEFAULT_PASSWORD = "NSe@2019";
    private static final String DEFAULT_EMAIL = "klapnp@klapnp.com";
    private static final String DEFAULT_FIRST_NAME = "FTFTFTFTFTFT";
    private static final String DEFAULT_PHONE_NUMBER = "45454545454578";
    private static final String DEFAULT_LOGIN = "LLLLLLLLLL";
    private RegisterUserVM userVM = new RegisterUserVM();
    private MvcResult mvcResult;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Before
    public void setUp() {
        //given
        userVM.setLogin(DEFAULT_LOGIN);
        userVM.setPhoneNumber(DEFAULT_PHONE_NUMBER);
        userVM.setFirstName(DEFAULT_FIRST_NAME);
        userVM.setLastName(DEFAULT_FIRST_NAME);
        userVM.setEmail(DEFAULT_EMAIL);
        userVM.setPassword(DEFAULT_PASSWORD);
    }

    @Test
    public void whenCreateAccount_userIsCreated() throws Exception {
        //given
        int databaseSizeBeforeCreate = userRepository.findAll().size();

        //when
        mvcResult = mvc.perform(MockMvcRequestBuilders.post("/public/account/")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsString(userVM)))
                .andExpect(status().isOk()).andReturn();
        //then
        assertThat(userRepository.findAll().size()).isEqualTo(databaseSizeBeforeCreate + 1);

    }

}