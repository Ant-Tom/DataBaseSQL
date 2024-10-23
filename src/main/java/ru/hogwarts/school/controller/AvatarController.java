package ru.hogwarts.school.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.service.AvatarService;

import java.io.IOException;

@RestController
@RequestMapping("/avatar")
public class AvatarController {
    private final AvatarService avatarService;

    public AvatarController(AvatarService avatarService) {
        this.avatarService = avatarService;
    }

    @PostMapping("/{studentId}")
    public ResponseEntity<Avatar> uploadAvatar(@PathVariable Long studentId, @RequestParam("file") MultipartFile file) {
        try {
            Avatar avatar = avatarService.uploadAvatar(studentId, file);
            return ResponseEntity.status(HttpStatus.CREATED).body(avatar);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<Avatar> getAvatarFromDatabase(@PathVariable Long studentId) {
        Avatar avatar = avatarService.getAvatarFromDatabase(studentId);
        if (avatar == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(avatar);
    }

    @GetMapping("/file/{fileName}")
    public ResponseEntity<byte[]> getAvatarFromDisk(@PathVariable String fileName) {
        try {
            byte[] data = avatarService.getAvatarFromDisk(fileName);
            return ResponseEntity.ok(data);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
