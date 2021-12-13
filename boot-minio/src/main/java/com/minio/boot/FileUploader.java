package com.minio.boot;

import io.minio.*;

import io.minio.errors.MinioException;


import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.InvalidKeyException;


public class FileUploader {

    public static void main(String[] args) throws NoSuchAlgorithmException, IOException, InvalidKeyException {
        try {
            final MinioClient minioClient = MinioClient.builder().endpoint("http://120.24.6.135:9000").credentials("minioadmin", "minioadmin").build();

            String bucketName = "asiatrip";
            // 检查存储桶是否已经存在
            final boolean bucketExists = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
            if (bucketExists) {
                System.out.println("Bucket already exists.");
            } else {
                // 创建一个名为asiatrip的存储桶，用于存储照片的zip文件
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            }

            final ObjectWriteResponse objectWriteResponse = minioClient.uploadObject(UploadObjectArgs.builder()
                    .bucket(bucketName)
                    .object("runPng")
                    .filename("E:\\Projects\\Git-Repostiys\\note\\boot-minio\\src\\main\\resources\\run.png")
                    .build());

            System.out.println(objectWriteResponse);
        } catch (MinioException e) {
            System.out.println("Error occurred: " + e);
            System.out.println("Error occurred: " + e.httpTrace());
        }
    }

}