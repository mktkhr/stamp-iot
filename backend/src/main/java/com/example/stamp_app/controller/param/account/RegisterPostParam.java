package com.example.stamp_app.controller.param.account;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
@Schema(description = "アカウント登録パラメータ")
public class RegisterPostParam {

    @NotNull
    @Schema(description = "メールアドレス", example = "aaa@example.com")
    @Pattern(regexp = "^([a-zA-Z0-9])+([a-zA-Z0-9._-])*@([a-zA-Z0-9_-])+([a-zA-Z0-9._-]+)+$")
    private String email;

    @NotNull
    @Schema(description = "パスワード", example = "SamplePassword")
    @Pattern(regexp = "^(?=.*[A-Z])[a-zA-Z0-9.?/-]{8,24}$")
    private String password;

}
