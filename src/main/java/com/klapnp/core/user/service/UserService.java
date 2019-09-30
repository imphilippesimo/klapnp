package com.klapnp.core.user.service;

import com.klapnp.core.user.model.User;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public interface UserService {

    Optional<User> activateRegistration(String key);

    User registerUser(User user, String password);

    void updateUser(User user);

    void deleteUser(String login);

    @Transactional
    Optional<User> getUserWithAuthoritiesByLogin(String login);

    @Transactional
    Optional<User> getUserWithAuthorities(Long id);

    List<String> getAuthorities();


}
