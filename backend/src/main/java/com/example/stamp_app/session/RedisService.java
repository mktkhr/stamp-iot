package com.example.stamp_app.session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;
import java.util.concurrent.TimeUnit;

@Service
public class RedisService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * セッション情報の削除
     *
     * @param key キー
     */
    @SuppressWarnings("unchecked")
    public void delete(String... key) {
        if (key != null && key.length > 0) {
            if (key.length == 1) {
                redisTemplate.delete(key[0]);
            } else {
                redisTemplate.delete((Collection<String>) CollectionUtils.arrayToList(key));
            }
        }
    }

    /**
     * セッション情報の取得
     *
     * @param key セッションUUID
     * @return String ユーザーUUID or null
     */
    public String getUserUuidFromSessionUuid(String key) {
        String value = null;

        try{
            value = (String) redisTemplate.opsForValue().get(key);
        }catch (Exception e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        // 空の場合，400を返す
        if(value == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        return value;
    }

    /**
     * セッション情報の保存
     *
     * @param key セッションUUID
     * @param value ユーザーUUID
     */
    public void set(String key, String value, long timeInSec) {
        redisTemplate.opsForValue().set(key, value, timeInSec, TimeUnit.SECONDS);
    }

}