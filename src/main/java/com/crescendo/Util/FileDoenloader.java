package com.crescendo.Util;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.FileOutputStream;
import java.io.IOException;

public class FileDoenloader {
    public static void downloadFile(String fileUrl, String saveFilePath) throws IOException {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<byte[]> response = restTemplate.getForEntity(fileUrl, byte[].class);

        if (response.getStatusCode() == HttpStatus.OK) {
            HttpHeaders headers = response.getHeaders();
            byte[] fileBytes = response.getBody();

            FileOutputStream outputStream = new FileOutputStream(saveFilePath);
            outputStream.write(fileBytes);
            outputStream.close();
        } else {
            // Handle error
        }
    }

}
