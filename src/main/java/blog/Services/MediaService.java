package blog.Services;

import blog.Model.enums.MediaType;
import blog.Model.Media;
import blog.Model.Post;
import blog.Repositories.MediaRepository;
import lombok.RequiredArgsConstructor;

import org.apache.tika.Tika;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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

    public Media saveBase64File(String dataUrl) throws Exception {
        byte[] fileBytes = decodeBase64(dataUrl);
        String mimeType = detectMimeType(fileBytes);
        FileInfo fileInfo = getFileExtensionAndMediaType(mimeType);

        String fileName = UUID.randomUUID() + fileInfo.extension;
        saveFileToDisk(fileBytes, fileName);

        return saveMedia(fileName, fileInfo.mediaType, null);
    }

    public void saveBase64File(Post post, String dataUrl, String outputPath) throws Exception {
        byte[] fileBytes = decodeBase64(dataUrl);
        String mimeType = detectMimeType(fileBytes);
        FileInfo fileInfo = getFileExtensionAndMediaType(mimeType);

        saveFileToDisk(fileBytes, outputPath + fileInfo.extension);
        saveMedia(outputPath + fileInfo.extension, fileInfo.mediaType, post);
    }

    public Media saveFile(MultipartFile file) throws IOException {
        return saveFile(file, null);
    }

    public Media saveImage(Post post, MultipartFile file) throws IOException {
        return saveFile(file, post);
    }

    public void removeImage(Long mediaId) {
        Media media = mediaRepository.findById(mediaId)
                .orElseThrow(() -> new IllegalArgumentException("Media not found"));
        deleteFile(media.getUrl());
        mediaRepository.delete(media);
    }

    public void removeImagesByPost(Post post) {
        post.getMedia().forEach(media -> {
            deleteFile(media.getUrl());
            mediaRepository.delete(media);
        });
    }

    // --- Private helpers ---

    private Media saveFile(MultipartFile file, Post post) throws IOException {
        String mimeType = file.getContentType();
        if (mimeType == null)
            throw new IllegalArgumentException("Cannot determine file type");

        FileInfo fileInfo = getFileExtensionAndMediaType(mimeType);
        String fileName = UUID.randomUUID() + fileInfo.extension;
        saveFileToDisk(file.getBytes(), fileName);

        return saveMedia(fileName, fileInfo.mediaType, post);
    }

    private Media saveMedia(String fileName, MediaType mediaType, Post post) {
        Media media = new Media();
        media.setUrl(fileName);
        media.setType(mediaType);
        media.setPost(post);
        return mediaRepository.save(media);
    }

    private void saveFileToDisk(byte[] bytes, String fileName) throws IOException {
        Path path = Paths.get(uploadDir + fileName);
        Files.write(path, bytes);
    }

    private void deleteFile(String fileName) {
        Path path = Paths.get(uploadDir + fileName);
        try {
            Files.deleteIfExists(path);
        } catch (IOException ignored) {
        }
    }

    private byte[] decodeBase64(String dataUrl) {
        String[] parts = dataUrl.split(",");
        if (parts.length != 2)
            throw new IllegalArgumentException("Invalid data URL.");
        return Base64.getDecoder().decode(parts[1]);
    }

    private String detectMimeType(byte[] fileBytes) throws IOException {
        String mimeType = tika.detect(fileBytes);
        if (mimeType == null || mimeType.isBlank()) {
            throw new IllegalArgumentException("Could not determine file type.");
        }
        System.out.println();
        System.out.println(mimeType);
        return mimeType;
    }

    private FileInfo getFileExtensionAndMediaType(String mimeType) {
        if (mimeType.startsWith("image/")) {
            return new FileInfo("." + mimeType.split("/")[1], MediaType.IMAGE);
        } else if (mimeType.equals("video/mp4") || mimeType.equals("video/quicktime")) {
            return new FileInfo(".mp4", MediaType.VIDEO);
        } else {
            throw new IllegalArgumentException("Unsupported file type: " + mimeType);
        }
    }

    private record FileInfo(String extension, MediaType mediaType) {
    }
}
