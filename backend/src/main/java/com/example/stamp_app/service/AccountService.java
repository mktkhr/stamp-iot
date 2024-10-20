package com.example.stamp_app.service;

import com.example.stamp_app.controller.param.account.LoginPostParam;
import com.example.stamp_app.controller.param.account.RegisterPostParam;
import com.example.stamp_app.controller.response.AccountGetResponse;
import com.example.stamp_app.controller.response.AccountLoginResponse;
import com.example.stamp_app.domain.exception.EMSDatabaseException;
import com.example.stamp_app.domain.exception.EMSResourceDuplicationException;
import com.example.stamp_app.domain.exception.EMSResourceNotFoundException;
import com.example.stamp_app.entity.Account;
import com.example.stamp_app.repository.AccountRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.springframework.util.DigestUtils.md5DigestAsHex;

@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class AccountService {
    @Autowired
    AccountRepository accountRepository;

    /**
     * アカウント追加Service
     *
     * @param registerPostParam 登録情報
     */
    public void addAccount(RegisterPostParam registerPostParam) {

        var isNewUser = accountRepository.findByEmail(registerPostParam.getEmail()) == null;

        if (!isNewUser) {
            log.error(" The email address has already been used.");
            throw new EMSResourceDuplicationException();
        }

        final var hashedPassword = DigestUtils.md5DigestAsHex(registerPostParam.getPassword().getBytes());
        final var localDateTime = LocalDateTime.now();
        final var newUser = new Account(
                null,
                UUID.randomUUID(),
                registerPostParam.getEmail(),
                hashedPassword,
                null,
                localDateTime,
                localDateTime,
                null,
                null
        );

        accountRepository.save(newUser);

    }

    /**
     * ログインService
     *
     * @param loginPostParam ログイン情報
     * @return HttpStatus
     */
    public AccountLoginResponse login(LoginPostParam loginPostParam) throws IllegalAccessException {

        var loginUser = accountRepository.findByEmailAndDeletedAtIsNull(loginPostParam.getEmail());

        // 対象のアカウントが存在しない場合，404を返す
        if (loginUser == null) {
            log.error("This account does not exist.");
            throw new EMSResourceNotFoundException();
        }

        boolean isCorrectPassword = loginUser.getPassword().matches(md5DigestAsHex(loginPostParam.getPassword().getBytes()));

        // パスワードが合致しない場合，401を返す
        if (!isCorrectPassword) {
            log.error("Account Information are not correct.");
            throw new IllegalAccessException();
        }

        log.info("Successfully logged in.");
        return new AccountLoginResponse(HttpStatus.OK, loginUser);

    }

    /**
     * アカウント情報取得Service
     *
     * @param userUuid ユーザーUUID
     * @return Account アカウント情報
     */
    public AccountGetResponse getAccountInfo(String userUuid) {

        var account = accountRepository.findByUuid(UUID.fromString(userUuid));

        // アカウントが存在しない場合，400を返す
        if (account == null) {
            log.error("This account does not exist.");
            throw new IllegalArgumentException();
        }

        // IDと名前のみを返す
        return new AccountGetResponse(account.getId(), account.getName(), account.getCreatedAt(), account.getUpdatedAt());
    }

    /**
     * アカウント削除Service
     *
     * @param userUuid ユーザーUUID
     */
    public void deleteAccount(String userUuid) {

        var account = accountRepository.findByUuid(UUID.fromString(userUuid));

        // アカウントが存在しない場合，400を返す
        if (account == null) {
            log.error("This account does not exist.");
            throw new IllegalArgumentException();
        }

        final var updatedAccount = new Account(
                account.getId(),
                account.getUuid(),
                account.getEmail(),
                account.getPassword(),
                account.getName(),
                account.getCreatedAt(),
                account.getUpdatedAt(),
                LocalDateTime.now(), // 論理削除フラグとして削除日時を追加
                account.getMicroController()
        );

        // 論理削除したデータで上書き
        accountRepository.save(updatedAccount);
    }
}
