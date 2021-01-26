package com.mrfisherman.library.model.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;

@Getter
@Setter
public class RegisterCredentials {

    private String username;
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$",
            message = "Password should have minimum eight characters," +
                    " at least one upper letter, one lowercase letter, one special character and one number")
    private String password;
    @Email(message = "Email should be valid")
    private String email;

}
