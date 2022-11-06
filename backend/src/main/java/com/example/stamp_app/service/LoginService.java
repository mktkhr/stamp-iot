package com.example.stamp_app.service;

import com.example.stamp_app.entity.Account;
import com.example.stamp_app.repository.AccountRepository;
import com.example.stamp_app.session.RedisService;
import com.example.stamp_app.session.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

import static org.springframework.util.DigestUtils.md5DigestAsHex;

@Service
public class LoginService {
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    RedisService redisService;
    @Autowired
    SessionService sessionService;

    /**
     * ログインService
     *
     * @param userData ログイン情報
     * @return HttpStatus
     */
    public HttpStatus login(Account userData, HttpServletResponse httpServletResponse) {

        boolean isCorrectPassword;

        if (userData == null) {
            return HttpStatus.BAD_REQUEST;
        }

        try {
            Account loginUser = accountRepository.findByEmail(userData.getEmail());

            if (loginUser == null) {
                System.out.println("This account is not exist.");
                return HttpStatus.BAD_REQUEST;
            }

            isCorrectPassword = loginUser.getPassword().matches(md5DigestAsHex(userData.getPassword().getBytes()));

            if (!isCorrectPassword) {
                System.out.println("Account Information are not correct.");
                return HttpStatus.BAD_REQUEST;
            }

            // redis
            String sessionId = UUID.randomUUID().toString();
            redisService.set(loginUser.getUlid().toString(), sessionId, 60 * 60);

            // cookie
            Cookie cookie = sessionService.generateCookie(sessionId);
            httpServletResponse.addCookie(cookie);

            System.out.println("Session ID:" + sessionId);
            System.out.println("Successfully logged in.");
            return HttpStatus.OK;

        } catch (Exception exception) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }

    }
}
