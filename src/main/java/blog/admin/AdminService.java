package blog.admin;

import org.springframework.stereotype.Service;

import blog.post.jpa.PostRepository;
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

    public void showAllUsers() {
        // view all users
    }

    public void showAllPosts() {
        // view all posts
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

    public void deletePost(Long postId) {
        // permanently delete post
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

    public void deleteUser(Long userId) {
        // permanently delete user
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
