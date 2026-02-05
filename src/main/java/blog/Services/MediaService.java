package blog.Services;

import blog.Model.enums.MediaType;
import blog.Model.Media;
import blog.Model.Post;
import blog.Repositories.MediaRepository;
import lombok.RequiredArgsConstructor;
import org.apache.tika.Tika;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MediaService {

    private final MediaRepository mediaRepository;
    private final String uploadDir = "src/main/resources/static/";
    private final Tika tika = new Tika();

    // --- Delete a single media file from disk and DB ---
    @Transactional
    public void delete(Media media) {
        deleteFile(media.getUrl());
        mediaRepository.delete(media);
    }

    // --- Remove all media for a post ---
    @Transactional
    public void removeImagesByPost(Post post) {
        if (post.getMedia() != null && !post.getMedia().isEmpty()) {
            post.getMedia().forEach(media -> deleteFile(media.getUrl()));
            post.getMedia().clear(); // orphanRemoval deletes DB rows
        }
    }

    // --- Save Base64 file and attach to post ---
    @Transactional
    public Media saveBase64File(Post post, String dataUrl, String outputPath) throws Exception {
        byte[] fileBytes = decodeBase64(dataUrl);
        String mimeType = detectMimeType(fileBytes);
        FileInfo fileInfo = getFileExtensionAndMediaType(mimeType);

        String fileName = outputPath + fileInfo.extension;
        saveFileToDisk(fileBytes, fileName);

        Media media = new Media();
        media.setPost(post);
        media.setType(fileInfo.mediaType);
        media.setUrl(fileName);

        post.getMedia().add(media); // add to post list
        return mediaRepository.save(media);
    }

    // --- Save uploaded file ---
    @Transactional
    public Media saveFile(MultipartFile file, Post post) throws IOException {
        String mimeType = file.getContentType();
        if (mimeType == null) throw new IllegalArgumentException("Cannot determine file type");

        FileInfo fileInfo = getFileExtensionAndMediaType(mimeType);
        String fileName = UUID.randomUUID() + fileInfo.extension;
        saveFileToDisk(file.getBytes(), fileName);

        Media media = new Media();
        media.setPost(post);
        media.setType(fileInfo.mediaType);
        media.setUrl(fileName);

        if (post != null) post.getMedia().add(media);
        return mediaRepository.save(media);
    }

    // --- Delete file from disk ---
    public void deleteFile(String fileName) {
        Path path = Paths.get(uploadDir + fileName);
        try {
            Files.deleteIfExists(path);
        } catch (IOException ignored) { }
    }

    // --- Save byte array to disk ---
    private void saveFileToDisk(byte[] bytes, String fileName) throws IOException {
        Path path = Paths.get(uploadDir + fileName);
        if (path.getParent() != null) {
            Files.createDirectories(path.getParent());
        }
        Files.write(path, bytes);
    }

    // --- Base64 helper ---
    private byte[] decodeBase64(String dataUrl) {
        String[] parts = dataUrl.split(",");
        if (parts.length != 2) throw new IllegalArgumentException("Invalid data URL.");
        return Base64.getDecoder().decode(parts[1]);
    }

    // --- Detect MIME type ---
    private String detectMimeType(byte[] fileBytes) throws IOException {
        String mimeType = tika.detect(fileBytes);
        if (mimeType == null || mimeType.isBlank()) {
            throw new IllegalArgumentException("Could not determine file type.");
        }
        return mimeType;
    }

    // --- Map MIME type to extension and MediaType ---
    private FileInfo getFileExtensionAndMediaType(String mimeType) {
        if (mimeType.startsWith("image/")) {
            return new FileInfo("." + mimeType.split("/")[1], MediaType.IMAGE);
        } else if (mimeType.equals("video/mp4") || mimeType.equals("video/quicktime")) {
            return new FileInfo(".mp4", MediaType.VIDEO);
        } else {
            throw new IllegalArgumentException("Unsupported file type: " + mimeType);
        }
    }

    // --- Helper record ---
    private record FileInfo(String extension, MediaType mediaType) { }
}
