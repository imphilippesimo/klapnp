package com.klapnp.core.usermanagement.exposition.endpoint;

import com.klapnp.core.notification.NotificationContext;
import com.klapnp.core.notification.service.contract.NotificationService;
import com.klapnp.core.usermanagement.exposition.vm.RegisterUserVM;
import com.klapnp.core.usermanagement.exposition.vm.mapper.RegisterUserMapper;
import com.klapnp.core.usermanagement.model.KlapUser;
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
    private NotificationService notificationService;

    public AccountResource(IUserService userService, RegisterUserMapper registerUserMapper, NotificationService notificationService) {
        this.userService = userService;
        this.registerUserMapper = registerUserMapper;
        this.notificationService = notificationService;
    }

    @PostMapping("/public/account")
    public void registerAccount(@Valid @RequestBody RegisterUserVM registerUserVM) {

        try {
            KlapUser user = userService.registerUser(registerUserMapper.vmToDTO(registerUserVM), registerUserVM.getPassword());
            //TODO Send notification for account validation
            NotificationContext<KlapUser> notificationContext = new NotificationContext<>();
            notificationContext.setTo(registerUserVM.getEmail());
            notificationContext.setEntity(user);
            notificationService.pushAccountCreation(notificationContext);
        } catch (LoginAlreadyUsedException le) {
            throw new AccountAlreadyExistsException(ErrorConstants.LOGIN_ALREADY_USED_TYPE, "Login is already in use!", "", "login-exists");
        } catch (EmailAlreadyUsedException ee) {
            throw new AccountAlreadyExistsException(ErrorConstants.EMAIL_ALREADY_USED_TYPE, "Email is already in use!", "", "email-exists");
        }


    }

    @GetMapping("/public/account/activate")
    public void activateAccount(@RequestParam(value = "key") String key) {
        userService.activateRegistration(key);

    }
}
