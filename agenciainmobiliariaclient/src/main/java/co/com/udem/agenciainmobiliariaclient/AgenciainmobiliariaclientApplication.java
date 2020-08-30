package co.com.udem.agenciainmobiliariaclient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import co.com.udem.agenciainmobiliariaclient.entities.UserToken;

@SpringBootApplication
@EnableEurekaClient
public class AgenciainmobiliariaclientApplication {

	public static void main(String[] args) {
		SpringApplication.run(AgenciainmobiliariaclientApplication.class, args);
	}

	@Bean
	RestTemplate restTemplate() {
		return new RestTemplate();
	}

	@Bean
	UserToken userToken() {
		return new UserToken();
	}
}
