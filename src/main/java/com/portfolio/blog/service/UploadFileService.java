package com.portfolio.blog.service;

import com.portfolio.blog.domain.Post;
import com.portfolio.blog.domain.UploadFile;
import com.portfolio.blog.dto.S3Dto;
import com.portfolio.blog.dto.UploadFileDto;
import com.portfolio.blog.exception.FileNotFoundException;
import com.portfolio.blog.exception.PostNotFoundException;
import com.portfolio.blog.exception.WrongFileTypeException;
import com.portfolio.blog.repo.PostRepository;
import com.portfolio.blog.repo.UploadFileRepository;
import com.portfolio.blog.s3.AwsS3Service;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UploadFileService {

    private final UploadFileRepository uploadFileRepository;
    private final AwsS3Service awsS3Service;

    @Transactional
    public String save(MultipartFile multipartFile) {
        String uuid = UUID.randomUUID().toString();

        S3Dto s3Dto = awsS3Service.upload(multipartFile, uuid);
        UploadFile uploadFile = UploadFile.builder()
                .filename(s3Dto.getFilename())
                .fileId(uuid)
                .fileUrl(s3Dto.getUploadUrl())
                .mimetype(multipartFile.getContentType()).build();

        uploadFileRepository.save(uploadFile);

        return uploadFile.getFileId();
    }

    @Transactional(readOnly = true)
    @SneakyThrows(IOException.class)
    public UploadFileDto findOne(String uuid) {
        UploadFile uploadFile = uploadFileRepository.findUploadFileByFileId(uuid)
                .orElseThrow(() -> new FileNotFoundException("file not in database"));
        UrlResource urlResource = new UrlResource(uploadFile.getFileUrl());
        return new UploadFileDto(uploadFile, urlResource);
    }

    @Transactional(readOnly = true)
    public List<UploadFileDto> findAll() {
        List<UploadFile> files = uploadFileRepository.findAll();
        List<UploadFileDto> collect = files.stream()
                .map(file -> new ModelMapper().map(file, UploadFileDto.class)).collect(Collectors.toList());
        return collect;
    }

    @Transactional
    public void delete(String fileId) {
        UploadFile uploadFile = uploadFileRepository.findUploadFileByFileId(fileId)
                .orElseThrow(() -> new FileNotFoundException("file not in database"));

        awsS3Service.remove(uploadFile.getFilename());
        uploadFileRepository.delete(uploadFile);
    }

    @Transactional
    public void deleteAll() {
        List<UploadFile> files = uploadFileRepository.findAll();
        List<String> filenames = files.stream().map(file -> file.getFilename()).collect(Collectors.toList());
        awsS3Service.removeAll(filenames);
        uploadFileRepository.deleteAll();
    }

}
