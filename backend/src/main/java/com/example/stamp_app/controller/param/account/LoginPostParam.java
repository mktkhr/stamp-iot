package com.example.stamp_app.controller.param.account;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class LoginPostParam {

    @Pattern(regexp = "^([a-zA-Z0-9])+([a-zA-Z0-9._-])*@([a-zA-Z0-9_-])+([a-zA-Z0-9._-]+)+$")
    @NotBlank
    private String email;

    // NOTE: ログイン時のパスワードはパターン推測を回避するため，パターンバリデーションを行わない
    @NotBlank
    private String password;

}
