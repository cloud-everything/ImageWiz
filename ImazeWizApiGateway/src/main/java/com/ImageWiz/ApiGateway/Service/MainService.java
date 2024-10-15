package com.ImageWiz.ApiGateway.Service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class MainService {

    @Value("${cloud.aws.s3.bucket-name}")
    private String bucketName;
    @Value("${cloud.aws.region.static}")
    private String region;
    @Value("${cloud.aws.credentials.access-key}")
    private String accessKey;
    @Value("${cloud.aws.credentials.secret-key}")
    private String secretKey;

    public Boolean sendImageToS3(MultipartFile file){
        if (file.isEmpty()) {
            return false;
        }
        S3Client s3Client = S3Client.builder()
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(accessKey, secretKey)))
                .build();
        try{
            String randomFileName = UUID.randomUUID().toString() + getFileExtension(file.getName());
            Path tempFile = Files.createTempFile(file.getOriginalFilename(), null);
            Files.copy(file.getInputStream(), tempFile, StandardCopyOption.REPLACE_EXISTING);

            PutObjectRequest putObjectRequest=PutObjectRequest.builder()
                    .bucket(bucketName).key(randomFileName).build();
            s3Client.putObject(putObjectRequest,tempFile);
            Files.delete(tempFile);
            return true;
        }
        catch (Exception err){
            System.out.println(err.getMessage());
            return false;
        }finally {
            s3Client.close();
        }
    }




    // Utility method to get file extension
    private static String getFileExtension(String fileName) {
        int lastIndexOfDot = fileName.lastIndexOf('.');
        if (lastIndexOfDot == -1) {
            return ""; // No extension
        }
        return fileName.substring(lastIndexOfDot);
    }

}
