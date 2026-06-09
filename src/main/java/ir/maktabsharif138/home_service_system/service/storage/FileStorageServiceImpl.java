package ir.maktabsharif138.home_service_system.service.storage;

import ir.maktabsharif138.home_service_system.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Service
@Slf4j
public class FileStorageServiceImpl implements FileStorageService {

    private static final long MAX_IMAGE_SIZE = 300 * 1024;
    private static final Set<String> ALLOWED_TYPES = Set.of(
            "image/jpeg",
            "image/png",
            "image/jpg"
    );
    private final Path uploadDirectory = Paths.get("uploads/profile-image");

    @Override
    public String saveProfileImage(MultipartFile image) {
        validateProfileImage(image);
        try {
            Files.createDirectories(uploadDirectory);
            String fileName = UUID.randomUUID() + "_" + image.getOriginalFilename();
            Path destination = uploadDirectory.resolve(fileName);
            Files.copy(image.getInputStream(),
                    destination,
                    StandardCopyOption.REPLACE_EXISTING
            );
            return fileName;
        } catch (IOException ex) {
            throw new BadRequestException("Could not store profile image");
        }
    }

    @Override
    public void validateProfileImage(MultipartFile image) {
        if (Objects.isNull(image) || image.isEmpty()) {
            throw new BadRequestException("Profile image is required");
        }
        if (image.getSize() > MAX_IMAGE_SIZE) {
            throw new BadRequestException("Profile image must be less than 300KB");
        }
        if (!ALLOWED_TYPES.contains(image.getContentType())) {
            throw new BadRequestException("Only jpg and png images are allowed");
        }
    }

    @Override
    public void delete(String fileName) {
        if (!StringUtils.hasText(fileName)) {
            return;
        }
        try {
            Path filePath = uploadDirectory.resolve(fileName);
            Files.deleteIfExists(filePath);
        } catch (IOException ex) {
            log.error("Could not delete file {}", fileName, ex);
        }
    }
}
