package br.com.desafioitau.order;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

@OpenAPIDefinition(
		info = @Info(title = "Order Microservice API",
				version = "1.0",
				description = "Order Microservice API")
)
@SpringBootApplication
@EnableFeignClients
@EnableRabbit
@EnableScheduling
public class OrderApplication {

	public static void main(String[] args) {
		SpringApplication.run(OrderApplication.class, args);
	}
}
