plugins {
	id("org.springframework.boot") version "3.3.3"
	id("io.spring.dependency-management") version "1.1.6"
	id("java")
	id("org.jetbrains.kotlin.jvm") version "2.0.21"
	id("org.jetbrains.kotlin.plugin.spring") version "2.0.21"
}

kotlin {
	jvmToolchain(21)
}

group = "com.example"

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.springframework.boot:spring-boot-starter-data-redis")

	// Flyway
	implementation("org.flywaydb:flyway-core:10.17.2")
	implementation("org.flywaydb:flyway-database-postgresql")

	// MyBatis
	implementation("org.mybatis:mybatis-spring:3.0.4")

	// Springdoc
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.1.0")

	// Actuator
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("io.micrometer:micrometer-registry-prometheus:1.13.4")

	compileOnly("org.projectlombok:lombok:1.18.34")
	annotationProcessor("org.projectlombok:lombok:1.18.34")
	developmentOnly("org.springframework.boot:spring-boot-devtools")
	runtimeOnly("org.postgresql:postgresql")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.security:spring-security-test")
	testImplementation("org.junit.jupiter:junit-jupiter:5.9.2")
	testImplementation("org.mockito:mockito-core:5.4.0")
	testImplementation("org.mockito.kotlin:mockito-kotlin:5.4.0")
	testImplementation("org.mockito.kotlin:mockito-inline:5.2.0")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
}

tasks.test {
	useJUnitPlatform()
	testLogging {
		showStandardStreams = true
		events("skipped", "passed", "failed")
		exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
	}
}

tasks.jar {
	enabled = false
}

tasks.bootRun {
	// RemoteDebug用の設定
	jvmArgs = listOf("-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005")
}
