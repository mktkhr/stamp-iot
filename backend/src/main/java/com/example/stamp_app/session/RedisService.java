package com.example.stamp_app.session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

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
     * @param key キー
     * @return String
     */
    public String get(String key) {
        return key == null ? null : (String) redisTemplate.opsForValue().get(key);
    }

    /**
     * セッション情報の保存
     *
     * @param key   キー
     * @param value 値
     */
    public void set(String key, String value, long timeInSec) {
        redisTemplate.opsForValue().set(key, value, timeInSec, TimeUnit.SECONDS);
    }

}