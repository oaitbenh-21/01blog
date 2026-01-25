package blog.Services;

import blog.Model.enums.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MediaService {

    @Autowired
    private MediaRepository mediaRepository;

    private final String uploadDir = "src/main/resources/static/";

    public void saveBase64File(Post post, String dataUrl, String outputPath) throws Exception {
        String[] parts = dataUrl.split(",");
        if (parts.length != 2) {
            throw new IllegalArgumentException("Invalid data URL.");
        }
        byte[] fileBytes = Base64.getDecoder().decode(parts[1]);
        String fileType = getMimeType(fileBytes);
        if (fileType == null)
            throw new IllegalArgumentException("Could not determine file type.");

        String ext = ".mp4";
        MediaType mediaType;

        switch (fileType) {
            case "image/png", "image/jpeg", "image/gif" -> {
                ext = "." + fileType.split("/")[1];
                mediaType = MediaType.IMAGE;
            }
            case "video/mp4" -> mediaType = MediaType.VIDEO;
            default -> throw new IllegalArgumentException("Unsupported file type: " + fileType);
        }
        saveMedia(post, outputPath + ext, mediaType);
        try (FileOutputStream fos = new FileOutputStream(new File(uploadDir + outputPath + ext))) {
            fos.write(fileBytes);
        }
    }

    public Media saveBase64File(String dataUrl) throws Exception {
        String[] parts = dataUrl.split(",");
        if (parts.length != 2)
            throw new IllegalArgumentException("Invalid data URL.");
        byte[] fileBytes = Base64.getDecoder().decode(parts[1]);

        String fileType = getMimeType(fileBytes);
        if (fileType == null)
            throw new IllegalArgumentException("Could not determine file type.");

        String ext = ".mp4";
        MediaType mediaType;

        switch (fileType) {
            case "image/png", "image/jpeg", "image/gif" -> {
                ext = "." + fileType.split("/")[1];
                mediaType = MediaType.IMAGE;
            }
            case "video/mp4" -> mediaType = MediaType.VIDEO;
            default -> throw new IllegalArgumentException("Unsupported file type: " + fileType);
        }

        String fileName = UUID.randomUUID() + ext;
        try (FileOutputStream fos = new FileOutputStream(new File(uploadDir + fileName))) {
            fos.write(fileBytes);
        }
        Media media = new Media();
        media.setType(mediaType);
        media.setUrl(fileName);
        return mediaRepository.save(media);
    }

    public Media saveImage(Post post, MultipartFile file) throws IOException {
        String mimeType = file.getContentType();
        if (mimeType == null)
            throw new IllegalArgumentException("Cannot determine file type");
        String ext;
        MediaType mediaType;

        if (mimeType.startsWith("image/")) {
            ext = mimeType.split("/")[1];
            mediaType = MediaType.IMAGE;
        } else if (mimeType.equals("video/mp4")) {
            ext = "mp4";
            mediaType = MediaType.VIDEO;
        } else {
            throw new IllegalArgumentException("Unsupported file type: " + mimeType);
        }
        String fileName = UUID.randomUUID() + "." + ext;
        Files.write(Paths.get(uploadDir + fileName), file.getBytes());
        Media media = new Media();
        media.setPost(post);
        media.setType(mediaType);
        media.setUrl(fileName);
        return mediaRepository.save(media);
    }

    public Media saveFile(MultipartFile file) throws IOException {
        String mimeType = file.getContentType();
        if (mimeType == null)
            throw new IllegalArgumentException("Cannot determine file type");
        String ext;
        MediaType mediaType;
        if (mimeType.startsWith("image/")) {
            ext = mimeType.split("/")[1];
            mediaType = MediaType.IMAGE;
        } else if (mimeType.equals("video/mp4")) {
            ext = "mp4";
            mediaType = MediaType.VIDEO;
        } else {
            throw new IllegalArgumentException("Unsupported file type: " + mimeType);
        }
        String fileName = UUID.randomUUID() + "." + ext;
        Files.write(Paths.get(uploadDir + fileName), file.getBytes());
        Media media = new Media();
        media.setType(mediaType);
        media.setUrl(fileName);
        return mediaRepository.save(media);
    }

    public String getMimeType(byte[] fileBytes) throws IOException {
        try (ByteArrayInputStream bis = new ByteArrayInputStream(fileBytes)) {
            return URLConnection.guessContentTypeFromStream(bis);
        }
    }

    public void saveMedia(Post post, String fileUrl, MediaType mediaType) {
        Media media = new Media();
        media.setType(mediaType);
        media.setPost(post);
        media.setUrl(fileUrl);
        mediaRepository.save(media);
    }

    public void removeImage(Long mediaId) {
        Media media = mediaRepository.findById(mediaId)
                .orElseThrow(() -> new IllegalArgumentException("Media not found"));
        Path filePath = Paths.get(uploadDir + media.getUrl());
        try {
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete file", e);
        }
        mediaRepository.delete(media);
    }

    public void removeImagesByPost(Post post) {
        post.getMedia().forEach(media -> {
            Path filePath = Paths.get(uploadDir + media.getUrl());
            try {
                Files.deleteIfExists(filePath);
            } catch (IOException ignored) {
            }
            mediaRepository.delete(media);
        });
    }
}
