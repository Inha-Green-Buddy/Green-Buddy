package com.keb.kebsmartfarm.config;

import com.keb.kebsmartfarm.Controller.KitController;
import com.keb.kebsmartfarm.constant.Message.Error;
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
            throw new IllegalStateException(Error.CAN_NOT_STORE_EMPTY_FILE);
        }

        String contentType = file.getContentType();
        if (contentType != null && !(contentType.contains("image/jpeg") || contentType.contains("image/png") || contentType.contains("image/gif")))
            throw new IllegalStateException(Error.NOT_IMAGE_FILE);

        Path destinationFile = rootLocation.resolve(
                        Paths.get(LocalDateTime.now().toString() + "-" + UUID.randomUUID() + "-" + file.getOriginalFilename()))
                .normalize().toAbsolutePath();

        log.info(destinationFile.toString());

        if (!destinationFile.getParent().equals(rootLocation.toAbsolutePath())) {
            // This is a security check
            throw new IllegalStateException(Error.CAN_NOT_STORE_OUTSIDE_OF_DIRECTORY);
        }
        return destinationFile;
    }
}
