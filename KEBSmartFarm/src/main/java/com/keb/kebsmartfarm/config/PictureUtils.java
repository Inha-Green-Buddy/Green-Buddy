package com.keb.kebsmartfarm.config;

import com.keb.kebsmartfarm.Controller.KitController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
public class PictureUtils {

    public static final Path rootLocation = Paths.get("src", "main", "resources", "pictures");

    public static String getFileExtension(String fileName) {
        String[] nameAndExt = fileName.split("\\.");
        return nameAndExt[nameAndExt.length - 1];
    }

    public static MediaType getMediaTypeForExtension(String extension) {
        return switch (extension.toLowerCase()) {
            case "png" -> MediaType.IMAGE_PNG;
            case "gif" -> MediaType.IMAGE_GIF;
            case "jpg", "jpeg" -> MediaType.IMAGE_JPEG;
            default -> throw new IllegalStateException("Unexpected value: " + extension.toLowerCase());
        };
    }

    public static String getUrl(Path storedPath) {
        return MvcUriComponentsBuilder.fromMethodName(KitController.class,
                "serveFile",
                storedPath.getFileName().toString()).build().toUri().toString();
    }

    public static Path getDestPath(MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalStateException("빈 파일은 저장할 수 없습니다.");
        }

        String contentType = file.getContentType();
        if (contentType != null && !(contentType.contains("image/jpeg") || contentType.contains("image/png") || contentType.contains("image/gif")))
            throw new IllegalStateException("이미지 파일이 아닙니다.");

        Path destinationFile = rootLocation.resolve(
                        Paths.get(LocalDateTime.now().toString() + "-" + UUID.randomUUID() + "-" + file.getOriginalFilename()))
                .normalize().toAbsolutePath();

        log.info(destinationFile.toString());

        if (!destinationFile.getParent().equals(rootLocation.toAbsolutePath())) {
            // This is a security check
            throw new IllegalStateException("파일은 현재 디렉토리 바깥에 저장될 수 없습니다.");
        }
        return destinationFile;
    }
}
