package my.dnd.app.start;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "my.dnd.app")
public class StartApplication {	
	public static void main(String[] args) {
		SpringApplication.run(StartApplication.class, args);
	}
}
