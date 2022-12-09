package com.example.stamp_app.service;

import com.example.stamp_app.controller.response.MicroControllerPostResponse;
import com.example.stamp_app.entity.Account;
import com.example.stamp_app.entity.MicroController;
import com.example.stamp_app.repository.AccountRepository;
import com.example.stamp_app.repository.MicroControllerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigInteger;

@Service
public class MicroControllerService {

    @Autowired
    MicroControllerRepository microControllerRepository;
    @Autowired
    AccountRepository accountRepository;

    public MicroControllerPostResponse addMicroControllerRelation(BigInteger userId, String macAddress) {

        MicroController microController;
        Account requestedAccount;

        try {
            // 対象のMAC Addressのマイクロコントローラー情報を取得
            microController = microControllerRepository.findByMacAddress(macAddress);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        // 対象のマイクロコントローラーがDB上に存在しなかった場合，400を返す
        if(microController == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        try {
            // リクエストしたアカウント情報を取得
            requestedAccount = accountRepository.findById(userId);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        // 対象のアカウントが存在しなかった場合，400を返す
        if (requestedAccount == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        // 既にマイクロコントローラーがアカウントに紐づけられていた場合，401を返す
        if (microController.getAccount() != null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        microController.setAccount(requestedAccount);

        try {
            // マイクロコントローラー情報を更新する
            microControllerRepository.save(microController);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new MicroControllerPostResponse(HttpStatus.OK, microController);

    }
}
