package com.rddev.fileserver.controller;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("files")
public class FileController {

    // https://stackoverflow.com/questions/35680932/download-a-file-from-spring-boot-rest-service

    @GetMapping("/large/download")
    public ResponseEntity<Resource> downloadLargeFile() throws FileNotFoundException {
        File file = new File("/home/user/Documents/file.zip");
        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_OCTET_STREAM).body(resource);
    }

    @GetMapping("/large2/download/{fileName}")
    public ResponseEntity<Resource> downloadLageFileByteArray(@PathVariable String fileName, HttpServletRequest request) throws IOException {
        Path path = Paths.get("/home/user/Documents/"+fileName);
        File file = new File(path.toUri());
        ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(path));
        String absolutePath = file.getAbsolutePath();
        String mimeType = request.getServletContext().getMimeType(absolutePath);
        MediaType contentType = mimeType == null ? MediaType.APPLICATION_OCTET_STREAM : MediaType.parseMediaType(mimeType);

        return ResponseEntity.ok().contentLength(file.length()).contentType(contentType).body(resource);
    }

}
