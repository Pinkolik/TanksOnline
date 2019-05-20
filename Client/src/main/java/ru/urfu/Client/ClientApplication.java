package ru.urfu.Client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;


@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class ClientApplication {
	public static void main(String[] args) throws Exception {
		new SpringApplicationBuilder(ClientApplication.class)
				.headless(false)
				.web(WebApplicationType.NONE)
				.run(args);
		LogInForm logInForm = new LogInForm();
	}

}
