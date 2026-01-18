package blog;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.crypto.password.PasswordEncoder;

import blog.Model.User;
import blog.Model.enums.Role;
import blog.Repositories.UserRepository;

@SpringBootApplication
@EnableJpaAuditing
public class BlogApplication {

	public static void main(String[] args) {
		SpringApplication.run(BlogApplication.class, args);
	}

	@Bean
	public CommandLineRunner cmdRunner(
			UserRepository repository,
			PasswordEncoder passwordEncoder) { // Inject as parameter, not field
		return args -> {
			User admin = new User();
			repository.findByUsername("oaitbenh").ifPresent(existingUser -> {
				return;
			});
			admin.setUsername("oaitbenh");
			admin.setEmail("0xmrrandom@gmail.com");
			admin.setPassword(passwordEncoder.encode("admin123")); // Use parameter
			admin.setRole(Role.ADMIN);
			repository.save(admin);
		};
	}
}
