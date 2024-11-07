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
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import org.apache.commons.codec.binary.Base64;

@Service
@RequiredArgsConstructor
@Log4j2
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class FileService {

    Cloudinary cloudinary;

    public String uploadFile(String base64File) {
        try {
            byte[] decodedBytes = Base64.decodeBase64(base64File);
            ByteArrayInputStream inputStream = new ByteArrayInputStream(decodedBytes);
            String fileName =  generateFileName();

            Map optios = com.cloudinary.utils.ObjectUtils.asMap(
                    "public_id", fileName,
                    "resource_type", "resource_type_image"
            );

            Map<String, Object> response = cloudinary.uploader().upload(inputStream, optios);

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

    public void deleteFile(List<String> fileName) {
        try {
            if(ObjectUtils.isEmpty(fileName)){
                return;
            }
            ApiResponse apiResponse = cloudinary.api().deleteResources(fileName,
                    com.cloudinary.utils.ObjectUtils.asMap("type", "upload",
                            "resource_type", "resource_type_image"));
            log.info(apiResponse);
        } catch (Exception exception) {
            throw new RuntimeException(exception.getMessage());
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
