package com.portfolio.blog.repo;

import com.portfolio.blog.domain.UploadFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UploadFileRepository extends JpaRepository<UploadFile, Long> {

    @Query("select u from UploadFile u where u.fileId = ?1")
    Optional<UploadFile> findUploadFileByFileId(String fileId);

}
