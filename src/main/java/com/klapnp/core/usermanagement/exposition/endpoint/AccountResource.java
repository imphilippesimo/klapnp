package com.klapnp.core.usermanagement.exposition.endpoint;

import com.klapnp.core.usermanagement.exposition.vm.RegisterUserVM;
import com.klapnp.core.usermanagement.exposition.vm.mapper.RegisterUserMapper;
import com.klapnp.core.usermanagement.service.contract.IUserService;
import com.klapnp.core.util.errors.AccountAlreadyExistsException;
import com.klapnp.core.util.errors.EmailAlreadyUsedException;
import com.klapnp.core.util.errors.ErrorConstants;
import com.klapnp.core.util.errors.LoginAlreadyUsedException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class AccountResource {

    private IUserService userService;
    private RegisterUserMapper registerUserMapper;

    public AccountResource(IUserService userService, RegisterUserMapper registerUserMapper) {
        this.userService = userService;
        this.registerUserMapper = registerUserMapper;
    }

    @PostMapping("/public/account")
    public void registerAccount(@Valid @RequestBody RegisterUserVM registerUserVM) {
        try {
            userService.registerUser(registerUserMapper.vmToDTO(registerUserVM), registerUserVM.getPassword());
        } catch (LoginAlreadyUsedException le) {
            throw new AccountAlreadyExistsException(ErrorConstants.LOGIN_ALREADY_USED_TYPE, "Login is already in use!", "", "login-exists");
        } catch (EmailAlreadyUsedException ee) {
            throw new AccountAlreadyExistsException(ErrorConstants.EMAIL_ALREADY_USED_TYPE, "Email is already in use!", "", "email-exists");
        }

        //TODO Send notification for account validation

    }

    @GetMapping("/public/account/activate")
    public void activateAccount(@RequestParam(value = "key") String key) {
        userService.activateRegistration(key);

    }
}
