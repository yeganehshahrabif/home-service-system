package ir.maktabsharif138.home_service_system.service.storage;

import ir.maktabsharif138.home_service_system.exception.BadRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class FileStorageServiceImplTest {

    private FileStorageServiceImpl service;

    @TempDir
    Path tempDir;

    private MockMultipartFile validImage;

    @BeforeEach
    void setUp() {
        // مهم: inject کردن tempDir به جای مسیر ثابت
        service = new FileStorageServiceImpl() {
            {
                try {
                    var field = FileStorageServiceImpl.class.getDeclaredField("uploadDirectory");
                    field.setAccessible(true);
                    field.set(this, tempDir);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        };

        validImage = new MockMultipartFile(
                "file",
                "test.jpg",
                "image/jpeg",
                "fake-image-content".getBytes()
        );
    }

    @Test
    void saveProfileImage_shouldSaveSuccessfully() {

        String fileName = service.saveProfileImage(validImage);

        assertNotNull(fileName);
        assertTrue(fileName.endsWith("_test.jpg"));
    }

    @Test
    void saveProfileImage_shouldThrow_whenFileInvalid() {

        MockMultipartFile invalid = new MockMultipartFile(
                "file",
                "test.jpg",
                "image/jpeg",
                new byte[0]
        );

        assertThrows(BadRequestException.class, () ->
                service.saveProfileImage(invalid));
    }

    @Test
    void validateProfileImage_shouldPass_whenValid() {

        assertDoesNotThrow(() ->
                service.validateProfileImage(validImage));
    }

    @Test
    void validateProfileImage_shouldThrow_whenNull() {

        assertThrows(BadRequestException.class, () ->
                service.validateProfileImage(null));
    }

    @Test
    void validateProfileImage_shouldThrow_whenEmpty() {

        MockMultipartFile empty = new MockMultipartFile(
                "file",
                "test.jpg",
                "image/jpeg",
                new byte[0]
        );

        assertThrows(BadRequestException.class, () ->
                service.validateProfileImage(empty));
    }

    @Test
    void validateProfileImage_shouldThrow_whenTooLarge() {

        byte[] bigFile = new byte[400 * 1024];

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test.jpg",
                "image/jpeg",
                bigFile
        );

        assertThrows(BadRequestException.class, () ->
                service.validateProfileImage(file));
    }

    @Test
    void validateProfileImage_shouldThrow_whenInvalidType() {

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test.txt",
                "text/plain",
                "data".getBytes()
        );

        assertThrows(BadRequestException.class, () ->
                service.validateProfileImage(file));
    }

    @Test
    void delete_shouldNotThrow_whenFileDoesNotExist() {

        assertDoesNotThrow(() ->
                service.delete("fake-file.jpg"));
    }

    @Test
    void delete_shouldDeleteFileIfExists() throws Exception {

        Path file = tempDir.resolve("test.jpg");
        Files.createFile(file);

        assertTrue(Files.exists(file));

        service.delete("test.jpg");

        assertFalse(Files.exists(file));
    }
}