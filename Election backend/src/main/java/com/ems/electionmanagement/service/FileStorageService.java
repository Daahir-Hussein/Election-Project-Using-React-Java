package com.ems.electionmanagement.service;

import com.ems.electionmanagement.exception.BadRequestException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

@Service
public class FileStorageService {

    private static final Set<String> ALLOWED_TYPES = Set.of("image/jpeg", "image/png");
    private static final Set<String> ALLOWED_EXTENSIONS = Set.of("jpg", "jpeg", "png");

    private final Path uploadRoot;

    public FileStorageService(@Value("${app.upload.dir}") String uploadDir) {
        this.uploadRoot = Path.of(uploadDir).toAbsolutePath().normalize();
    }

    public String storeImage(MultipartFile file, String category) {
        if (file == null || file.isEmpty()) {
            throw new BadRequestException("No file selected.");
        }
        if (!ALLOWED_TYPES.contains(file.getContentType())) {
            throw new BadRequestException("Only .jpg, .jpeg, and .png image files are allowed.");
        }

        String originalName = StringUtils.cleanPath(
            file.getOriginalFilename() == null ? "image" : file.getOriginalFilename()
        );
        String extension = extensionOf(originalName);
        if (!ALLOWED_EXTENSIONS.contains(extension)) {
            throw new BadRequestException("Only .jpg, .jpeg, and .png image files are allowed.");
        }

        String fileName = UUID.randomUUID() + "." + extension;
        Path categoryDirectory = uploadRoot.resolve(category).normalize();
        Path target = categoryDirectory.resolve(fileName).normalize();

        if (!target.startsWith(categoryDirectory)) {
            throw new BadRequestException("Invalid file path.");
        }

        try {
            Files.createDirectories(categoryDirectory);
            Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);
            return fileName;
        } catch (IOException ex) {
            throw new BadRequestException("The image could not be stored.");
        }
    }

    public Path getUploadRoot() {
        return uploadRoot;
    }

    private String extensionOf(String filename) {
        int dotIndex = filename.lastIndexOf('.');
        if (dotIndex < 0 || dotIndex == filename.length() - 1) {
            return "";
        }
        return filename.substring(dotIndex + 1).toLowerCase(Locale.ROOT);
    }
}
