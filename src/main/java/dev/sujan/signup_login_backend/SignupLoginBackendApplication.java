package dev.sujan.signup_login_backend;

import dev.sujan.signup_login_backend.user.Role;
import dev.sujan.signup_login_backend.user.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class SignupLoginBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(SignupLoginBackendApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(RoleRepository repository){
		return args -> repository.save(
				Role.builder().name("user").build()
		);
	}

}
