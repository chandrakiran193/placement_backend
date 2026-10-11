package com.placement.service;

import com.placement.exception.BadRequestException;
import com.placement.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.*;
import java.util.Objects;
import java.util.UUID;

@Service
public class FileStorageService {

    private final Path fileStorageLocation;

    public FileStorageService(@Value("${app.upload.dir:uploads/resumes}") String uploadDir) {
        this.fileStorageLocation = Paths.get(uploadDir).toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new RuntimeException("Could not create the upload directory at " + this.fileStorageLocation, ex);
        }
    }

    public String storeResume(MultipartFile file) {
        String originalFileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));

        if (originalFileName.contains("..")) {
            throw new BadRequestException("Filename contains invalid path sequence: " + originalFileName);
        }

        String lowerName = originalFileName.toLowerCase();
        if (!lowerName.endsWith(".pdf") && !lowerName.endsWith(".doc") && !lowerName.endsWith(".docx")) {
            throw new BadRequestException("Only PDF and DOC/DOCX files are supported for resumes.");
        }

        String storedFileName = UUID.randomUUID().toString() + "_" + originalFileName.replaceAll("[^a-zA-Z0-9.-]", "_");

        try {
            Path targetLocation = this.fileStorageLocation.resolve(storedFileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            return storedFileName;
        } catch (IOException ex) {
            throw new RuntimeException("Could not store file " + originalFileName + ". Please try again!", ex);
        }
    }

    public Resource loadFileAsResource(String fileName) {
        try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists() && resource.isReadable()) {
                return resource;
            } else {
                throw new ResourceNotFoundException("Resume file not found: " + fileName);
            }
        } catch (MalformedURLException ex) {
            throw new ResourceNotFoundException("Resume file not found: " + fileName);
        }
    }

    public void deleteFile(String fileName) {
        try {
            if (fileName != null) {
                Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
                Files.deleteIfExists(filePath);
            }
        } catch (IOException ignored) {
        }
    }
}
