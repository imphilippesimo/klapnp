package com.klapnp.core.usermanagement.service.impl;

import com.klapnp.core.usermanagement.model.KlapUser;
import com.klapnp.core.usermanagement.repository.UserRepository;
import com.klapnp.core.usermanagement.service.contract.IUserService;
import com.klapnp.core.usermanagement.service.dto.UserDTO;
import com.klapnp.core.usermanagement.service.mapper.UserDTOMapper;
import com.klapnp.core.util.Constants;
import com.klapnp.core.util.RandomUtil;
import com.klapnp.core.util.errors.EmailAlreadyUsedException;
import com.klapnp.core.util.errors.LoginAlreadyUsedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service
@Transactional
public class UserService implements IUserService, UserDetailsService {

    private final Logger log = LoggerFactory.getLogger(UserService.class);

    private UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private UserDTOMapper userDTOMapper = UserDTOMapper.INSTANCE;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public Optional<KlapUser> activateRegistration(String key) {
        log.debug("Activating user for activation key {}", key);
        return userRepository.findOneByActivationKey(key)
                .map(user -> {
                    // activate given user for the registration key.
                    user.setActivated(true);
                    user.setActivationKey(null);
                    log.debug("Activated user: {}", user);
                    return user;
                });
    }

    @Override
    public KlapUser registerUser(UserDTO userDTO, String password) {
        userRepository.findOneByLogin(userDTO.getLogin().toLowerCase()).ifPresent(existingUser -> {
            boolean removed = removeNonActivatedUser(existingUser);
            if (!removed) {
                throw new LoginAlreadyUsedException();
            }
        });
        userRepository.findOneByEmailIgnoreCase(userDTO.getEmail()).ifPresent(existingUser -> {
            boolean removed = removeNonActivatedUser(existingUser);
            if (!removed) {
                throw new EmailAlreadyUsedException();
            }
        });
        KlapUser newUser = userDTOMapper.UserDTOToUser(userDTO);
        String encodedPassword = passwordEncoder.encode(password);
        newUser.setPassword(encodedPassword);
        newUser.setActivated(false);
        newUser.setActivationKey(RandomUtil.generateActivationKey());
        newUser.setRole(Constants.ROLE_USER);
        userRepository.save(newUser);
        log.debug("Created Information for User: {}", newUser);
        return newUser;
    }

    @Override
    public Optional<UserDTO> updateUser(UserDTO userDTO) {
        return Optional.of(userRepository
                .findById(userDTO.getId()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(user -> userDTOMapper.UserToUserDTO(user));
    }

    @Override
    public void deleteUser(String login) {
        userRepository.findOneByLogin(login).ifPresent(user -> {
            userRepository.delete(user);
            log.debug("Deleted User: {}", user);
        });

    }

    /**
     * Not activated users should be automatically deleted after 3 days.
     * <p>
     * This is scheduled to get fired everyday, at 01:00 (am).
     */
    @Scheduled(cron = "0 0 1 * * ?")
    private boolean removeNonActivatedUser(KlapUser existingUser) {
        if (existingUser.isActivated()) {
            return false;
        }
        userRepository.delete(existingUser);
        userRepository.flush();
        return true;
    }

    @Override
    public UserDetails loadUserByUsername(String userNameOrEmailOrPhone) throws UsernameNotFoundException {
        Optional<KlapUser> foundByLogin = userRepository.findOneByLogin(userNameOrEmailOrPhone);
        Optional<KlapUser> foundByEmail = userRepository.findOneByEmailIgnoreCase(userNameOrEmailOrPhone);
        Optional<KlapUser> foundByPhoneNumber = userRepository.findOneByPhoneNumber(userNameOrEmailOrPhone);
        if (foundByLogin.isPresent() || foundByEmail.isPresent() || foundByPhoneNumber.isPresent()) {
            return getSpringUser(foundByLogin.get());
        } else {
            // If user not found. Throw this exception.
            throw new UsernameNotFoundException("User with info: " + userNameOrEmailOrPhone + " not found");
        }
    }

    private User getSpringUser(KlapUser foundUser) {
        List<GrantedAuthority> grantedAuthorities = AuthorityUtils.commaSeparatedStringToAuthorityList(foundUser.getRole());
        return new User(foundUser.getLogin(), foundUser.getPassword(), grantedAuthorities);
    }
}
