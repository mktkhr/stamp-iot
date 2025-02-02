package com.example.stamp_app.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.test.context.ActiveProfiles
import javax.sql.DataSource

@Configuration
@ActiveProfiles("unit-test")
@ConfigurationProperties(prefix = "spring.datasource")
class TestDatabaseConfig {
	var url: String? = null
	var username: String? = null
	var password: String? = null
	var driverClassName: String? = null

	@Bean
	fun dataSource(): DataSource {
		return DataSourceBuilder.create()
			.url(url)
			.username(username)
			.password(password)
			.driverClassName(driverClassName)
			.build()
	}
}