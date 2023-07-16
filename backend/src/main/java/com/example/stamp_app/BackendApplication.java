package com.example.stamp_app;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
		servers = {
				@Server(url = "http://localhost:8080", description = "ローカル環境"),
				@Server(url = "https://www.ems-engineering.jp", description = "本番環境")
		},
		info = @Info(
				title = "Stamp IoT API仕様書",
				description = "Stamp IoT用バックエンドアプリ",
				version = "v1"))
public class BackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
	}

}
