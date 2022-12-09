package com.example.stamp_app.controller.param.account;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class LoginPostParam {

    @NotNull
    @Pattern(regexp = "^([a-zA-Z0-9])+([a-zA-Z0-9._-])*@([a-zA-Z0-9_-])+([a-zA-Z0-9._-]+)+$")
    private String email;

    @NotNull
    @Pattern(regexp = "^(?=.*[A-Z])[a-zA-Z0-9.?/-]{8,24}$")
    private String password;

}
