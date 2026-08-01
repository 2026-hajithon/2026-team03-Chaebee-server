package hajiton.chaebee;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class ChaebeeApplication {

	public static void main(String[] args) {
		SpringApplication.run(ChaebeeApplication.class, args);
	}

}
