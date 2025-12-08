package blog.admin;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
public class AdminController {
    @DeleteMapping("/user/del/{uuid}")
    public ResponseEntity<String> deleteUser(@PathVariable UUID uuid) {

        return ResponseEntity.ok("uuid "+uuid);
    }
}
