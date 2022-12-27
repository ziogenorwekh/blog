package com.portfolio.blog.dto;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.portfolio.blog.domain.UploadFile;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.core.io.UrlResource;

import java.io.Serializable;
import java.util.Date;

/**
 * A DTO for the {@link UploadFile} entity
 */
@Data
@NoArgsConstructor
@JsonFilter("files")
public class UploadFileDto implements Serializable {
    private Long id;
    private String fileId;
    private String filename;
    private String mimetype;
    private Date insertDate;
    @JsonIgnore
    private UrlResource urlResource;

    public UploadFileDto(UploadFile file, UrlResource urlResource) {
        this.id = file.getId();
        this.fileId = file.getFileId();
        this.filename = file.getFilename();
        this.mimetype = file.getMimetype();
        this.insertDate = file.getInsertDate();
        this.urlResource = urlResource;
    }
}