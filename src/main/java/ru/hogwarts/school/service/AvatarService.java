package ru.hogwarts.school.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.AvatarRepository;
import ru.hogwarts.school.repository.StudentRepository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class AvatarService {
    private final AvatarRepository avatarRepository;
    private final StudentRepository studentRepository;

    @Autowired
    public AvatarService(AvatarRepository avatarRepository, StudentRepository studentRepository) {
        this.avatarRepository = avatarRepository;
        this.studentRepository = studentRepository;
    }

    public Avatar uploadAvatar(Long studentId, MultipartFile file) throws IOException {
        Student student = studentRepository.findById(studentId).orElseThrow(() -> new RuntimeException("Student not found"));

        Avatar avatar = new Avatar();
        avatar.setFilePath(file.getOriginalFilename());
        avatar.setFileSize(file.getSize());
        avatar.setMediaType(file.getContentType());
        avatar.setData(file.getBytes());

        // Save to local disk (example path, change as needed)
        Path localPath = Path.of("uploads/" + file.getOriginalFilename());
        Files.createDirectories(localPath.getParent());
        Files.write(localPath, file.getBytes());

        avatar.setStudent(student);
        return avatarRepository.save(avatar);
    }

    public Avatar getAvatarFromDatabase(Long studentId) {
        return avatarRepository.findByStudentId(studentId);
    }

    public byte[] getAvatarFromDisk(String fileName) throws IOException {
        Path path = Path.of("uploads/" + fileName);
        return Files.readAllBytes(path);
    }
}
