package com.example.ContentManagementService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/content")
public class ContentController {
    @Autowired
    private ContentRepository contentRepository;

    @PostMapping("/upload")
    public ResponseEntity<Void> uploadContent(@RequestParam("file") MultipartFile file, @RequestBody Content content) throws IOException {
        contentRepository.uploadContent(file, content);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/{contentId}")
    public ResponseEntity<Content> getContent(@PathVariable String contentId) {
        // Retrieve content metadata by content ID from your database or repository
        // ...

        if (content != null) {
            return ResponseEntity.ok(content);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/download/{s3ObjectKey:.+}")
    public ResponseEntity<byte[]> downloadContent(@PathVariable String s3ObjectKey) throws IOException {
        ResponseInputStream<GetObjectResponse> objectResponse = contentRepository.downloadContent(s3ObjectKey);
        byte[] contentBytes = objectResponse.readAllBytes();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDisposition(ContentDisposition.builder("attachment").filename(s3ObjectKey).build());

        return new ResponseEntity<>(contentBytes, headers, HttpStatus.OK);
    }

    @GetMapping("/list")
    public ResponseEntity<List<Content>> listAllContent() {
        List<Content> contentList = contentRepository.getAllContent();
        return ResponseEntity.ok(contentList);
    }

    // Implement endpoints for updating and deleting content
}

