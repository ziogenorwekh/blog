package com.portfolio.blog.s3;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.portfolio.blog.domain.UploadFile;
import com.portfolio.blog.dto.S3Dto;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class AwsS3Service {

    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    // MultipartFile -> File 전환 후 S3 업로드
    public S3Dto upload(MultipartFile multipartFile, String uuid) {
        File uploadFile = this.convert(multipartFile)
                .orElseThrow(() -> new IllegalArgumentException("convert failed"));
        return upload(uploadFile, uuid);
    }

    // S3 파일 삭제
    public void remove(String filename) {
        amazonS3Client.deleteObject(this.bucket,filename);
    }

    public void removeAll(List<String> files) {
        files.stream().forEach(s -> {
            amazonS3Client.deleteObject(this.bucket, s);
        });
    }

    private S3Dto upload(File uploadFile, String uuid) {
        String fileName = uuid + "_" + uploadFile.getName();
        String uploadImageUrl = this.putS3(uploadFile, fileName);
        this.removeLocalFile(uploadFile);  // 로컬에 생성된 File 삭제
        return new S3Dto(uploadImageUrl, fileName);
    }

    // S3 업로드
    private String putS3(File uploadFile, String fileName) {
        amazonS3Client.putObject(
                new PutObjectRequest(this.bucket, fileName, uploadFile)
                        .withCannedAcl(CannedAccessControlList.PublicRead)
        );
//        URL 리턴
        return amazonS3Client.getUrl(this.bucket, fileName).toString();
    }

    private void removeLocalFile(File targetFile) {
        targetFile.delete();
    }

    @SneakyThrows(IOException.class)
    private Optional<File> convert(MultipartFile file) {
        File convertFile = new File(file.getOriginalFilename());
        if (convertFile.createNewFile()) {
            try (FileOutputStream fileOutputStream = new FileOutputStream(convertFile)) {
                fileOutputStream.write(file.getBytes());
            }
            return Optional.of(convertFile);
        }
        return Optional.empty();
    }
}
