package com.ImageWiz.ApiGateway.Controller;

import com.ImageWiz.ApiGateway.Service.MainService;
import com.sun.tools.javac.Main;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import java.util.UUID;
import java.io.IOException;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

@RestController
public class MainController {
    MainService mainService;
    MainController(MainService mainService){
        this.mainService=mainService;
    }


    @PostMapping("/imageToBeCompressed")
    public ResponseEntity<successObject> postCompressedImage(@RequestParam("image") MultipartFile file){
        successObject response=new successObject();

        if(mainService.sendImageToS3(file)){
            response.setMessage("success");
        }
        else {
            response.setMessage("failed please check input");
        }
        return ResponseEntity.ok(response);
    }
    

    @PostMapping("/imageToBeCartoonised")
    public ResponseEntity<successObject> postCartoonisedImage(@RequestParam("image") MultipartFile file){
        successObject response=new successObject();
        if(mainService.sendImageToS3(file)){
            response.setMessage("success");
        }
        else {
            response.setMessage("failed please check input");
        }
        return ResponseEntity.ok(response);
    }

    public  static class successObject{
        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
        private String message;
    }


}

