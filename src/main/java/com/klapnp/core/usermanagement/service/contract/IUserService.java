package com.klapnp.core.usermanagement.service.contract;

import com.klapnp.core.usermanagement.model.KlapUser;
import com.klapnp.core.usermanagement.service.dto.UserDTO;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Transactional
public interface IUserService {

    Optional<KlapUser> activateRegistration(String key);

    KlapUser registerUser(UserDTO user, String password);

    Optional<UserDTO> updateUser(UserDTO userDTO);

    void deleteUser(String login);


}
