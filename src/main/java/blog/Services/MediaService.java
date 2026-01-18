package blog.Services;

import blog.Model.enums.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import blog.Model.Media;
import blog.Model.Post;
import blog.Repositories.MediaRepository;
import lombok.RequiredArgsConstructor;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

@Service
@RequiredArgsConstructor
public class MediaService {
    @Autowired
    private MediaRepository mediaRepository;
    private String uploadDir = "src/main/resources/static/";

    public void saveBase64File(Post post, String dataUrl, String outputPath) throws Exception {
        String[] parts = dataUrl.split(",");
        if (parts.length != 2) {
            throw new IllegalArgumentException("Invalid data URL.");
        }
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        String base64String = parts[1];
        byte[] fileBytes = Base64.getDecoder().decode(base64String);
        String fileType = this.getMimeType(fileBytes);
        if (fileType == null) {
            throw new IllegalArgumentException("Could not determine file type.");
        }
        String ext = ".mp4";
        MediaType mediaType;
        switch (fileType) {
            case "image/png", "image/jpeg", "image/gif" -> {
                ext = "." + fileType.split("/")[1];
                mediaType = MediaType.IMAGE;
            }
            case "video/mp4" -> {
                mediaType = MediaType.VIDEO;
            }
            default -> throw new IllegalArgumentException("Unsupported file type: " + fileType + ext);
        }

        System.out.println(outputPath + ext);
        this.saveMedia(post, outputPath + ext, mediaType);
        System.out.println(outputPath + ext);
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();

        try (FileOutputStream fos = new FileOutputStream(new File(uploadDir + outputPath + ext))) {
            fos.write(fileBytes);
        }
    }

    public String getMimeType(byte[] fileBytes) throws IOException {
        try (ByteArrayInputStream bis = new ByteArrayInputStream(fileBytes)) {
            return URLConnection.guessContentTypeFromStream(bis);
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

    public void saveMedia(Post post, String fileUrl, MediaType mediaType) {
        Media media = new Media();
        media.setType(mediaType);
        media.setPost(post);
        media.setUrl(fileUrl);
        mediaRepository.save(media);
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
