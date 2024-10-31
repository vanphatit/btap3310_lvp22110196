package me.phatlee.btap3110.controllers.common;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@RestController
public class UploadFile {
    @Value("${upload.directory:D:/upload}") // Đường dẫn tải lên từ application.properties
    private String uploadDirectory;
    @GetMapping("/image")
    public ResponseEntity<byte[]> getImage(@RequestParam String fname) throws IOException {
        System.out.println("Upload Directory: " + uploadDirectory);
        System.out.println("File Name: " + fname);

        File file = new File(uploadDirectory, fname);
        if (file.exists()) {
            byte[] imageBytes = IOUtils.toByteArray(new FileInputStream(file));
            MediaType mediaType = MediaType.IMAGE_JPEG; // Mặc định là JPEG
            if (fname.endsWith(".png")) {
                mediaType = MediaType.IMAGE_PNG;
            } else if (fname.endsWith(".gif")) {
                mediaType = MediaType.IMAGE_GIF;
            }
            return ResponseEntity.ok()
                    .contentType(mediaType)
                    .body(imageBytes);
        } else {
            System.out.println("File not found: " + file.getAbsolutePath());
            return ResponseEntity.notFound().build(); // Trả về 404 nếu file không tồn tại
        }
    }
}