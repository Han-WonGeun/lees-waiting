package waiting.lees_waiting;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
public class LeesWaitingApplication {

	public static void main(String[] args) {
		SpringApplication.run(LeesWaitingApplication.class, args);
	}

}
