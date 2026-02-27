package org.jragent.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;

public interface DocumentStorageService {
    Path getFilePath(String filePath);

    boolean fileExists(String filePath);

    String saveFile(String kbId, String documentId, MultipartFile file) throws IOException;

    void deleteFile(String filePath) throws IOException;
}
