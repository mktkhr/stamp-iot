package com.example.stamp_app.service;

import com.example.stamp_app.controller.param.microController.MicroControllerPatchParam;
import com.example.stamp_app.controller.response.MicroControllerGetResponse;
import com.example.stamp_app.controller.response.MicroControllerPostResponse;
import com.example.stamp_app.entity.Account;
import com.example.stamp_app.entity.MicroController;
import com.example.stamp_app.repository.AccountRepository;
import com.example.stamp_app.repository.MicroControllerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@Slf4j
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
        if (microController == null) {
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

    /**
     * アカウントに紐づくマイコンを取得
     *
     * @param userUuid ユーザーID
     * @return アカウントに紐づくマイコンリスト
     */
    public List<MicroControllerGetResponse> getMicroControllerList(String userUuid) {
        Account account;

        try {
            account = accountRepository.findByUuid(UUID.fromString(userUuid));
        } catch (Exception e) {
            log.error(e.toString());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        // アカウントが存在しなかった場合，400を返す
        if (account == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        var microControllerList = account.getMicroController();

        return MicroControllerGetResponse.convertMicroControllerToListResponse(microControllerList);
    }

    /**
     * マイコン詳細を取得
     *
     * @param microControllerUuid マイコンID
     * @return マイコン詳細
     */
    public MicroController getMicroControllerDetail(String microControllerUuid) {
        MicroController microController;

        try {
            microController = microControllerRepository.findByUuid(microControllerUuid);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        // アカウントが存在しなかった場合，400を返す
        if (microController == null) {
            log.error("該当のマイクロコントローラーの取得に失敗 UUID: " + microControllerUuid);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        return microController;
    }

    /**
     * マイコン詳細を更新
     *
     * @param param 更新用パラメータ
     * @return 更新後のマイコン詳細
     */
    @Transactional(rollbackFor = Exception.class)
    public MicroController updateMicroControllerDetail(String userUuid, MicroControllerPatchParam param) {
        MicroController microController;

        try {
            microController = microControllerRepository.findByUuid(param.getMicroControllerUuid());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        // マイコンが存在しなかった場合，400を返す
        if (microController == null) {
            log.error("該当のマイクロコントローラーの取得に失敗 マイコンUUID: " + param.getMicroControllerUuid());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        // マイコン所有者とリクエストしたアカウントが一致しない場合，400を返す
        if (!Objects.equals(microController.getAccount().getUuid().toString(), userUuid)) {
            log.error("マイコン所有者とリクエスト内容の不一致 マイコンUUID: " + param.getMicroControllerUuid());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }


        // パラメータのセット
        if (param.getName() != null) {
            microController.setName(param.getName());
        }

        microController.setInterval(param.getInterval());

        if (param.getSdi12Address() != null) {
            microController.setSdi12Address(param.getSdi12Address());
        }
        microController.setUpdatedAt(LocalDateTime.now());

        try {
            microControllerRepository.save(microController);
        } catch (Exception e) {
            log.error(e.toString());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        MicroController microControllerUpdateResult;
        try {
            microControllerUpdateResult = microControllerRepository.findByUuid(param.getMicroControllerUuid());
        } catch (Exception e) {
            log.error(e.toString());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }


        return microControllerUpdateResult;
    }
}
