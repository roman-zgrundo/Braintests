package by.zgrundo.braintests;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BraintestsApplication {

	public static void main(String[] args) {
		SpringApplication.run(BraintestsApplication.class, args);
	}

}
