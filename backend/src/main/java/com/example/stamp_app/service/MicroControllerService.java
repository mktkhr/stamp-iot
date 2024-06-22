package com.example.stamp_app.service;

import com.example.stamp_app.controller.param.microController.MicroControllerPatchParam;
import com.example.stamp_app.controller.response.MicroControllerGetResponse;
import com.example.stamp_app.controller.response.MicroControllerPostResponse;
import com.example.stamp_app.domain.exception.EMSDatabaseException;
import com.example.stamp_app.domain.exception.EMSResourceDuplicationException;
import com.example.stamp_app.domain.exception.EMSResourceNotFoundException;
import com.example.stamp_app.entity.Account;
import com.example.stamp_app.entity.MicroController;
import com.example.stamp_app.entity.RequestedUser;
import com.example.stamp_app.repository.AccountRepository;
import com.example.stamp_app.repository.MicroControllerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    @Autowired
    RequestedUser requestedUser;

    public MicroControllerPostResponse addMicroControllerRelation(Long userId, String macAddress) {

        // 対象のMAC Addressのマイクロコントローラー情報を取得
        var microController = microControllerRepository.findByMacAddress(macAddress);

        // 対象のマイクロコントローラーがDB上に存在しなかった場合，404を返す
        if (microController == null) {
            throw new EMSResourceNotFoundException();
        }

        // リクエストしたアカウント情報を取得
        var requestedAccount = accountRepository.findById(userId);

        // 対象のアカウントが存在しなかった場合，400を返す
        if (requestedAccount == null) {
            throw new EMSResourceNotFoundException();
        }

        // 既にマイクロコントローラーがアカウントに紐づけられていた場合，401を返す
        if (microController.getAccount() != null) {
            throw new EMSResourceDuplicationException();
        }

        microController.setAccount(requestedAccount);

        // マイクロコントローラー情報を更新する
        microControllerRepository.save(microController);

        return new MicroControllerPostResponse(microController.getId(), microController.getUuid(), microController.getName(), microController.getMacAddress(), microController.getInterval(), microController.getSdi12Address(), microController.getCreatedAt(), microController.getUpdatedAt(), microController.getDeletedAt());

    }

    /**
     * アカウントに紐づくマイコンを取得
     *
     * @param userUuid ユーザーID
     * @return アカウントに紐づくマイコンリスト
     */
    public List<MicroControllerGetResponse> getMicroControllerList(String userUuid) {

        final var account = accountRepository.findByUuid(UUID.fromString(userUuid));

        // アカウントが存在しなかった場合，404を返す
        if (account == null) {
            log.error("アカウントが存在しない");
            throw new EMSResourceNotFoundException();
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

        final var microController = microControllerRepository.findByUuid(microControllerUuid);

        // マイコンが存在しなかった場合，400を返す
        if (microController == null) {
            log.error("該当のマイクロコントローラーの取得に失敗 UUID: " + microControllerUuid);
            throw new EMSResourceNotFoundException();
        }

        // マイコン所有者とリクエストユーザーが異なる場合，404を返す
        if (!Objects.equals(microController.getAccount().getUuid().toString(), requestedUser.getUserUuid())) {
            log.error("マイコン所有者とリクエストユーザーの不一致");
            throw new EMSResourceNotFoundException();
        }

        return microController;
    }

    /**
     * マイコン詳細を取得(MACアドレス)
     *
     * @param macAddress マイコンID
     * @return マイコン詳細
     */
    public MicroController getMicroControllerDetailWithMacAddress(String macAddress) {

        var microController = microControllerRepository.findByMacAddress(macAddress);

        // マイコンが存在しなかった場合，404を返す
        if (microController == null) {
            log.error("該当のマイクロコントローラーの取得に失敗 MACアドレス: " + macAddress);
            throw new EMSResourceNotFoundException();
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

        var microController = microControllerRepository.findByUuid(param.getMicroControllerUuid());

        // マイコンが存在しなかった場合，404を返す
        if (microController == null) {
            log.error("該当のマイクロコントローラーの取得に失敗 マイコンUUID: " + param.getMicroControllerUuid());
            throw new EMSResourceNotFoundException();
        }

        // マイコン所有者とリクエストしたアカウントが一致しない場合，404を返す
        if (!Objects.equals(microController.getAccount().getUuid().toString(), userUuid)) {
            log.error("マイコン所有者とリクエストユーザーの不一致 マイコンUUID: " + param.getMicroControllerUuid());
            throw new EMSResourceNotFoundException();
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

        microControllerRepository.save(microController);

        return microControllerRepository.findByUuid(param.getMicroControllerUuid());
    }
}
