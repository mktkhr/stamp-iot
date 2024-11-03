package com.example.stamp_app.controller.param.account;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Schema(description = "ログインパラメータ")
public class LoginPostParam {

    @NotBlank
    @Schema(description = "メールアドレス", example = "aaa@example.com")
    @Pattern(regexp = "^([a-zA-Z0-9])+([a-zA-Z0-9._-])*@([a-zA-Z0-9_-])+([a-zA-Z0-9._-]+)+$")
    private String email;

    // NOTE: ログイン時のパスワードはパターン推測を回避するため，パターンバリデーションを行わない
    @NotBlank
    @Schema(description = "パスワード", example = "SamplePassword")
    private String password;

}
