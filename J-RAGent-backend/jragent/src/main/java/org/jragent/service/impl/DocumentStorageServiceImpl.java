package org.jragent.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
@Slf4j
public class DocumentStorageServiceImpl implements org.jragent.service.DocumentStorageService {

    @Value("${document.storage.base-path:./data/documents}")
    private String baseStoragePath;

    @Override
    public Path getFilePath(String filePath) {
        return Paths.get(baseStoragePath, filePath);
    }

    @Override
    public boolean fileExists(String filePath) {
        Path fullPath = getFilePath(filePath);
        return Files.exists(fullPath) && Files.isRegularFile(fullPath);
    }

    @Override
    public String saveFile(String kbId, String documentId, MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("上传的文件为空");
        }

        Path kbDir = Paths.get(baseStoragePath, kbId);
        Path documentDir = kbDir.resolve(documentId);

        Files.createDirectories(documentDir);

        String fileName = file.getOriginalFilename();
        String extension = "";
        if (fileName != null && fileName.contains(".")) {
            extension = fileName.substring(fileName.lastIndexOf("."));
        }
        String uniqueFileName = UUID.randomUUID().toString() + extension;

        Path targetPath = documentDir.resolve(uniqueFileName);
        Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);

        String relativePath = Paths.get(kbId, documentId,  uniqueFileName).toString();
        log.info("文件保存成功: kbId={}, documentId={}, filename={}, path={}",
                kbId, documentId, fileName, relativePath);

        return relativePath;
    }

    @Override
    public void deleteFile(String filePath) throws IOException {
        Path fullPath = getFilePath(filePath);
        if (Files.exists(fullPath)) {
            Files.delete(fullPath);
            log.info("文件删除成功: {}", fullPath);

            Path parentDir = fullPath.getParent();
            if (parentDir != null && Files.exists(parentDir)) {
                try {
                    Files.delete(parentDir);
                    log.info("目录删除成功: {}", parentDir);
                } catch (IOException e) {
                    log.debug("目录删除失败，请检查目录是否为空: {}", parentDir);
                }
            }
        } else {
            log.warn("文件不存在，跳过删除: {}", filePath);
        }
    }
}
