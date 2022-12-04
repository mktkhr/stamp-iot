package com.example.stamp_app.service;

import com.example.stamp_app.controller.Response.AccountInfoResponse;
import com.example.stamp_app.entity.Account;
import com.example.stamp_app.repository.AccountRepository;
import com.example.stamp_app.session.RedisService;
import com.example.stamp_app.session.SessionService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.web.client.HttpServerErrorException;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.springframework.util.DigestUtils.md5DigestAsHex;

@Service
public class AccountService {
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    RedisService redisService;
    @Autowired
    SessionService sessionService;

    /**
     * アカウント追加Service
     *
     * @param userData 登録情報
     * @return HttpStatus
     */
    public HttpStatus addAccount(Account userData) {

        boolean isNewUser;

        if (userData == null) {
            return HttpStatus.BAD_REQUEST;
        }

        try {
            isNewUser = accountRepository.findByEmail(userData.getEmail()) == null;
        } catch (Exception exception) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }

        if (!isNewUser) {
            System.out.println(" The email address has already been used.");
            return HttpStatus.CONFLICT;
        }

        String hashedPassword = DigestUtils.md5DigestAsHex(userData.getPassword().getBytes());
        LocalDateTime localDateTime = LocalDateTime.now();
        Account newUser = new Account();
        newUser.setUuid(UUID.randomUUID());
        newUser.setEmail(userData.getEmail());
        newUser.setPassword(hashedPassword);
        newUser.setCreatedAt(localDateTime);
        newUser.setUpdatedAt(localDateTime);

        try {
            accountRepository.save(newUser);
        } catch (Exception exception) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }

        System.out.println("Successfully registered.");
        return HttpStatus.OK;
    }

    /**
     * ログインService
     *
     * @param userData ログイン情報
     * @return HttpStatus
     */
    public Account login(Account userData, HttpServletResponse httpServletResponse) {

        boolean isCorrectPassword;

        try {
            Account loginUser = accountRepository.findByEmail(userData.getEmail());

            if (loginUser == null) {
                System.out.println("This account does not exist.");
                return null;
            }

            isCorrectPassword = loginUser.getPassword().matches(md5DigestAsHex(userData.getPassword().getBytes()));

            if (!isCorrectPassword) {
                System.out.println("Account Information are not correct.");
                return null;
            }

            System.out.println("Successfully logged in.");
            return loginUser;

        } catch (Exception exception) {
            throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    /**
     * アカウント情報取得Service
     *
     * @param UserUuid ユーザーUUID
     * @return Account アカウント情報
     */
    public AccountInfoResponse getAccountInfo(String UserUuid) {

        Account account;

        try {
            account = accountRepository.findByUuid(UUID.fromString(UserUuid));
            if (account == null) {
                System.out.println("This account does not exist.");
                return null;
            }

        } catch (Exception exception) {
            throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        // IDと名前のみを返す
        var accountInfoResponse = new AccountInfoResponse();
        accountInfoResponse.setId(account.getId());
        accountInfoResponse.setName(account.getName());

        return accountInfoResponse;
    }
}
