package ir.maktabsharif138.home_service_system.service.storage;

import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {

    String saveProfileImage(MultipartFile image);

    void validateProfileImage(MultipartFile image);

    void delete(String path);
}
