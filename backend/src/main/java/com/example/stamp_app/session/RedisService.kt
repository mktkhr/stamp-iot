package com.example.stamp_app.session

import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service
import java.util.concurrent.TimeUnit

@Service
class RedisService(
	private val redisTemplate: RedisTemplate<String, Any>
) {

	/**
	 * セッション情報の削除
	 *
	 * @param key キー
	 */
	fun delete(key: String?) {
		if (key != null) {
			redisTemplate.delete(key)
		}
	}

	/**
	 * セッション情報の取得
	 *
	 * @param key セッションUUID
	 * @return String ユーザーUUID or null
	 */
	@Throws(IllegalArgumentException::class)
	fun getUserUuidFromSessionUuid(key: String?): String? {
		if (key == null) {
			throw IllegalArgumentException("Session key must not be null.")
		}

		return redisTemplate.opsForValue()[key] as String?
	}

	/**
	 * セッション情報の保存
	 *
	 * @param key   セッションUUID
	 * @param value ユーザーUUID
	 */
	@Throws(IllegalArgumentException::class)
	operator fun set(key: String?, value: String, timeInSec: Long) {
		if (key == null) {
			throw IllegalArgumentException("Session key must not be null.")
		}

		redisTemplate.opsForValue()[key, value, timeInSec] = TimeUnit.SECONDS
	}
}