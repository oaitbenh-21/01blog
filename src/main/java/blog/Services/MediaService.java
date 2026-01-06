package blog.Services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class MediaService {

    // Directory to save uploaded files
    @Value("${media.upload.dir:uploads}")
    private String uploadDir;

    /**
     * Save the uploaded file and return its accessible URL/path.
     */
    public String uploadFile(MultipartFile file) {
        try {
            // Ensure the uploads directory exists
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // Clean file name
            String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
            String filename = System.currentTimeMillis() + "_" + originalFilename;

            // Save the file to the uploads folder
            Path filePath = uploadPath.resolve(filename);
            file.transferTo(filePath.toFile());

            // Return relative path or URL
            return "/media/" + filename;

        } catch (IOException e) {
            throw new RuntimeException("Failed to store file: " + e.getMessage(), e);
        }
    }

    /**
     * Retrieve a file as a byte array.
     */
    public byte[] getFile(String filename) {
        try {
            Path filePath = Paths.get(uploadDir).resolve(filename);
            if (!Files.exists(filePath)) {
                throw new RuntimeException("File not found: " + filename);
            }
            return Files.readAllBytes(filePath);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read file: " + e.getMessage(), e);
        }
    }

    /**
     * Optional: Delete a file
     */
    public void deleteFile(String filename) {
        Path filePath = Paths.get(uploadDir).resolve(filename);
        try {
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete file: " + filename, e);
        }
    }
}
