package blog;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.crypto.password.PasswordEncoder;

import blog.Model.*;
import blog.Model.enums.Role;
import blog.Repositories.*;

import java.util.*;

@SpringBootApplication
@EnableJpaAuditing
public class BlogApplication {

    public static void main(String[] args) {
        SpringApplication.run(BlogApplication.class, args);
    }

    @Bean
    public CommandLineRunner cmdRunner(
            UserRepository userRepository,
            PostRepository postRepository,
            CommentRepository commentRepository,
            LikeRepository likeRepository,
            PasswordEncoder passwordEncoder) {
        return args -> {

            if (userRepository.count() == 0) {

                // -------------------------
                // Create 10 users + 1 admin
                // -------------------------
                List<User> users = new ArrayList<>();
                for (int i = 1; i <= 10; i++) {
                    User user = new User();
                    user.setUsername("bot__" + i);
                    user.setEmail("bot_@" + i + "@example.com");
                    user.setPassword(passwordEncoder.encode("bot__" + i));
                    user.setRole(Role.USER);
                    userRepository.save(user);
                    users.add(user);
                }

                User admin = new User();
                admin.setUsername("oaitbenh");
                admin.setEmail("admin@example.com");
                admin.setPassword(passwordEncoder.encode("oaitbenh"));
                admin.setRole(Role.ADMIN);
                userRepository.save(admin);
                users.add(admin);

                Random random = new Random();

                // -------------------------
                // Create 20 posts
                // -------------------------
                List<Post> posts = new ArrayList<>();
                for (int i = 1; i <= 20; i++) {
                    Post post = new Post();
                    post.setUser(users.get(random.nextInt(users.size())));
                    post.setContent("This is post number " + i + " content.");
                    post.setDescription("Description for post " + i);
                    postRepository.save(post);
                    posts.add(post);
                }

                // -------------------------
                // Add 50 comments and 10–20 likes per post
                // -------------------------
                for (Post post : posts) {

                    // 50 comments
                    for (int c = 1; c <= 50; c++) {
                        Comment comment = new Comment();
                        comment.setPost(post);
                        comment.setUser(users.get(random.nextInt(users.size())));
                        comment.setContent("Comment " + c + " on post " + post.getId());
                        commentRepository.save(comment);
                    }

                    // 10–20 likes
                    int likesCount = 10 + random.nextInt(11); // random between 10–20
                    Set<User> likedUsers = new HashSet<>();
                    while (likedUsers.size() < likesCount) {
                        User randomUser = users.get(random.nextInt(users.size()));
                        if (!likedUsers.contains(randomUser)) {
                            Like like = new Like();
                            like.setPost(post);
                            like.setUser(randomUser);
                            likeRepository.save(like);
                            likedUsers.add(randomUser);
                        }
                    }
                }
            }
        };
    }
}
