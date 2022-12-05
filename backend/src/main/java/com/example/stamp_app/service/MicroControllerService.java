package com.example.stamp_app.service;

import com.example.stamp_app.controller.Response.MicroControllerPostResponse;
import com.example.stamp_app.entity.Account;
import com.example.stamp_app.entity.MicroController;
import com.example.stamp_app.repository.AccountRepository;
import com.example.stamp_app.repository.MicroControllerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

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

        try{
            // 対象のMAC Addressのマイクロコントローラー情報を取得
            microController = microControllerRepository.findByMacAddress(macAddress);
        }catch (Exception e){
            System.out.println(e);
            return new MicroControllerPostResponse(HttpStatus.INTERNAL_SERVER_ERROR, null);
        }


        // 対象のマイクロコントローラーがDB上に存在しなかった場合，400を返す
        if(microController == null){
            return new MicroControllerPostResponse(HttpStatus.BAD_REQUEST, null);
        }

        try {
            // リクエストしたアカウント情報を取得
            requestedAccount = accountRepository.findById(userId);
        }catch(Exception e){
            System.out.println(e);
            return new MicroControllerPostResponse(HttpStatus.INTERNAL_SERVER_ERROR, null);
        }

        // 対象のアカウントが存在しなかった場合，400を返す
        if(requestedAccount == null){
            return new MicroControllerPostResponse(HttpStatus.BAD_REQUEST, null);
        }

        // 既にマイクロコントローラーがアカウントに紐づけられていた場合，401を返す
        if(microController.getAccount() != null){
            return new MicroControllerPostResponse(HttpStatus.UNAUTHORIZED, null);
        }

        microController.setAccount(requestedAccount);

        try{
            // マイクロコントローラー情報を更新する
            microControllerRepository.save(microController);
        }catch(Exception e) {
            System.out.println(e);
            return new MicroControllerPostResponse(HttpStatus.INTERNAL_SERVER_ERROR, null);
        }

        return new MicroControllerPostResponse(HttpStatus.UNAUTHORIZED, microController);

    }
}
