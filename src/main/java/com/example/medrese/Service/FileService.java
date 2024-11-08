package com.example.medrese.Service;

import com.cloudinary.Cloudinary;
import com.cloudinary.api.ApiResponse;
import com.example.medrese.Core.Util.RandomUtil;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Pattern;

import org.apache.commons.codec.binary.Base64;

@Service
@RequiredArgsConstructor
@Log4j2
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class FileService {

    Cloudinary cloudinary;

    public String uploadFile(String base64File) {
        if (ObjectUtils.isEmpty(base64File)) {
            throw new IllegalArgumentException("base64File can not be null");
        }

        try {
            String fileName = generateFileName();

            File file = convertToFile(base64File, fileName);

            Map optios = com.cloudinary.utils.ObjectUtils.asMap(
                    "public_id", fileName,
                    "resource_type", "image"
            );

            Map<String, Object> response = cloudinary.uploader().upload(file, optios);

            String path = (String) response.getOrDefault("secure_url", "");
            String extension = (String) response.getOrDefault("format", "");
            String name = (String) response.getOrDefault("display_name", "");

            if (ObjectUtils.isEmpty(path) || ObjectUtils.isEmpty(extension) || ObjectUtils.isEmpty(name)) {
                throw new RuntimeException();
            }

            return path;
        } catch (IOException e) {
            log.error("Error while uploading file", e);
            throw new RuntimeException();
        }
    }

    public void deleteFile(String fileName) {
        if (ObjectUtils.isEmpty(fileName)) {
            return;
        }
        ArrayList<String> list = new ArrayList<>();
        list.add(fileName);
        deleteFile(list);
    }

    public void deleteFile(List<String> fileName) {
        try {
            if (ObjectUtils.isEmpty(fileName)) {
                return;
            }

            ApiResponse apiResponse = cloudinary.api().deleteResources(fileName.stream()
                            .map(path -> {
                                String name = path.substring(path.lastIndexOf('/') + 1);
                                return name.substring(0, name.lastIndexOf('.'));
                            })
                            .toList(),
                    com.cloudinary.utils.ObjectUtils.asMap("type", "upload",
                            "resource_type", "image"));
            log.info(apiResponse);
        } catch (Exception exception) {
            log.error("Error while deleting file", exception);
//            throw new RuntimeException(exception.getMessage());
        }
    }


    public void createDirectoryIfNotExists(String uploadDir) {
        Path path = Paths.get(uploadDir);
        if (!Files.exists(path)) {
            try {
                Files.createDirectories(path);
            } catch (IOException e) {
                log.error("Error while creating directory", e);
                throw new RuntimeException();
            }
        }
    }

    public File convertToFile(MultipartFile file) {
        try {
            File convFile = new File(System.getProperty("java.io.tmpdir") + "/" + file.getOriginalFilename());
            file.transferTo(convFile);
            return convFile;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public String generateFileName() {


        String formattedDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"));


        StringBuilder sb = new StringBuilder();


        return sb.append(RandomUtil.getUUIDAsStringWithoutDash())
                .append("_").append(formattedDateTime)
                .toString();
    }

    private boolean isBase64(String base64) {
        final Pattern BASE64_PATTERN = Pattern.compile("^[A-Za-z0-9+/]+={0,2}$");

        if (Objects.isNull(base64) || base64.length() % 4 != 0 || !BASE64_PATTERN.matcher(base64).matches()) {
            return false;
        }

        try {
            java.util.Base64.getDecoder().decode(base64);
            return true;
        } catch (IllegalArgumentException e) {
            log.error(e.getMessage());
            return false;
        }
    }

    public File convertToFile(String base64String, String fileName) {
        try {
            // Remove Base64 prefix if present (e.g., "data:image/png;base64,")
            if (base64String.contains(",")) {
                base64String = base64String.split(",")[1];
            }

            // Decode Base64 string to byte array
            byte[] decodedBytes = Base64.decodeBase64(base64String);

            // Create a temporary file
            File tempFile = new File(System.getProperty("java.io.tmpdir") + "/" + fileName);

            // Write decoded bytes to the file
            try (FileOutputStream fos = new FileOutputStream(tempFile)) {
                fos.write(decodedBytes);
            }

            return tempFile;
        } catch (IOException e) {
            throw new RuntimeException("Failed to convert Base64 string to file: " + e.getMessage(), e);
        }
    }
//    public String generateFileNameWithExtension() {
//        String newFileName = generateFileName();
//        String fileExtension = getFileExtension(newFileName);
//        return newFileName + "." + fileExtension;
//    }
//
//    public String getFileExtension(String fileName) {
//        if (fileName == null || fileName.isEmpty()) {
//            return "";
//        }
//
//        int lastDotIndex = fileName.lastIndexOf(".");
//        if (lastDotIndex > 0 && lastDotIndex < fileName.length() - 1) {
//            return fileName.substring(lastDotIndex + 1);
//        }
//
//        return "";
//    }
}
