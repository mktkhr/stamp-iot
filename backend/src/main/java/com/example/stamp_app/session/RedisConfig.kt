package com.example.stamp_app.session

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.serializer.StringRedisSerializer

@Configuration
open class RedisConfig {
    @Bean
    fun redisTemplate(factory: RedisConnectionFactory?): RedisTemplate<String, Any> {
        val template = RedisTemplate<String, Any>()
        val stringRedisSerializer = StringRedisSerializer()

        template.connectionFactory = factory
        template.keySerializer = stringRedisSerializer
        template.hashKeySerializer = stringRedisSerializer
        template.afterPropertiesSet()

        return template
    }
}
