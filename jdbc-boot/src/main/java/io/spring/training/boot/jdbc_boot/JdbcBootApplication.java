package io.spring.training.boot.jdbc_boot;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootApplication
public class JdbcBootApplication {

	public static void main(String[] args) {
		SpringApplication.run(JdbcBootApplication.class, args);
	}

	@Bean
	CommandLineRunner commandLineRunner(JdbcTemplate jdbcTemplate) {
		String query = "SELECT count(*) FROM T_ACCOUNT";

		// Use Lambda expression to display the result
		return args -> System.out.println("Hello, there are "
				+ jdbcTemplate.queryForObject(query, Long.class)
				+ " accounts");
	}
}
