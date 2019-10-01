package com.klapnp.core.usermanagement.exposition.vm;

import com.klapnp.core.util.Constants;

import javax.validation.constraints.*;

public class RegisterUserVM {

    @NotNull
    @Size(min = 1, max = 50)
    private String login;

    @Size(max = 50)
    @NotBlank
    private String firstName;

    @Size(max = 50)
    @NotBlank
    private String lastName;

    @Email
    @NotBlank
    @Size(min = 5, max = 254)
    private String email;

    @NotBlank
    @Pattern(regexp = Constants.INTL_PHONE_NUMBER_REGEX, message = Constants.INTL_PHONE_NUMBER_REGEX_MSG)
    private String phoneNumber;

    @Pattern(regexp = Constants.PASSWORD_REGEX, message = Constants.PASSWORD_REGEX_MSG)
    private String password;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
