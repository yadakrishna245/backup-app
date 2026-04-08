package com.backup;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.*;

@RestController
public class Controller {

    @PostMapping("/upload")
    public String upload(@RequestParam("file") MultipartFile file) throws Exception {
        File f = new File("/tmp/" + file.getOriginalFilename());
        file.transferTo(f);
        return "Uploaded";
    }

    @GetMapping("/restore/{name}")
    public byte[] restore(@PathVariable String name) throws Exception {
        File f = new File("/tmp/" + name);
        return java.nio.file.Files.readAllBytes(f.toPath());
    }
}
