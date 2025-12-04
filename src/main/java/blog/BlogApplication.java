package blog;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.crypto.password.PasswordEncoder;

import blog.user.jpa.UserEntity;
import blog.user.jpa.UserRepository;
import blog.user.jpa.utils.Role;

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
			var admin = UserEntity.builder()
					.fullname("Omar Ait Benhammou")
					.username("oaitbenh")
					.email("0xmrrandom@gmail.com")
					.password(passwordEncoder.encode("admin123")) // Use parameter
					.role(Role.ADMIN)
					.build();
			repository.save(admin);
		};
	}

}
