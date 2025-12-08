package blog.admin;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import blog.post.jpa.PostEntity;
import blog.post.jpa.PostRepository;
import blog.user.jpa.UserEntity;
import blog.user.jpa.UserRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AdminService {

    private final UserRepository userRepo;
    private final PostRepository postRepo;
    // private final ReportRepository reportRepo;

    /*
     * ============================
     * VIEWING / LISTING CONTENT
     * ============================
     */

    public List<UserEntity> showAllUsers() {
        return userRepo.findAll();
    }

    public List<PostEntity> showAllPosts() {
        return postRepo.findAll();
    }

    public void showAllReports() {
        // view all submitted reports
    }

    public void showAll() {
        // view users + posts + reports
    }

    /*
     * ============================
     * POST MODERATION
     * ============================
     */

    public boolean deletePost(UUID post_id) {
        if (postRepo.existsById(post_id)) {
            postRepo.deleteById(post_id);
            return true;
        }
        return false;
    }

    public void hidePost(Long postId) {
        // hide post (soft delete)
    }

    public void unhidePost(Long postId) {
        // restore hidden post
    }

    /*
     * ============================
     * USER MODERATION
     * ============================
     */

    public boolean deleteUser(UUID user_id) {
        if (userRepo.existsById(user_id)) {
            userRepo.deleteById(user_id);
            return true;
        }
        return false;
    }

    public void banUser(Long userId) {
        // ban user account
    }

    public void unbanUser(Long userId) {
        // unban user account
    }

    /*
     * ============================
     * REPORT HANDLING
     * ============================
     */

    public void resolveReport(Long reportId) {
        // mark report as resolved
    }

    public void deleteReport(Long reportId) {
        // delete a report
    }

}
