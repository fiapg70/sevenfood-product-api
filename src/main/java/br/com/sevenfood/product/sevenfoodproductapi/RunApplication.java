package br.com.sevenfood.product.sevenfoodproductapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "br.com.sevenfood.product.sevenfoodproductapi.infrastructure.repository")
@EntityScan(basePackages = "br.com.sevenfood.product.sevenfoodproductapi.infrastructure.entity")
public class RunApplication {

	public static void main(String[] args) {
		SpringApplication.run(RunApplication.class, args);
	}

}
