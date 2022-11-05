package com.example.stamp_app.service;

import com.example.stamp_app.entity.Account;
import com.example.stamp_app.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class RegisterService {
    @Autowired
    AccountRepository accountRepository;

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
        newUser.setUlid(UUID.randomUUID());
        newUser.setEmail(userData.getEmail());
        newUser.setPassword(hashedPassword);
        newUser.setCreatedAt(localDateTime);

        try {
            accountRepository.save(newUser);
        } catch (Exception exception) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }

        System.out.println("Successfully registered.");
        return HttpStatus.OK;
    }
}
