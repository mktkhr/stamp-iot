package com.example.stamp_app.config

import com.example.stamp_app.common.interceptor.AppInterceptor
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class BeanConfig {
	@Bean
	@Throws(Exception::class)
	fun appInterceptor(): AppInterceptor {
		return AppInterceptor()
	}
}
