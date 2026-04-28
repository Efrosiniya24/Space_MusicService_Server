package by.space.personalization;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = "by.space.personalization.preferences.entity")
@EnableJpaRepositories(basePackages = "by.space.personalization.preferences.repository")
public class PersonalizationApplication {

	public static void main(String[] args) {
		SpringApplication.run(PersonalizationApplication.class, args);
	}

}
