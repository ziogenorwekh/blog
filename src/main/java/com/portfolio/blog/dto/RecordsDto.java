package com.portfolio.blog.dto;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.portfolio.blog.domain.Records;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * A DTO for the {@link com.portfolio.blog.domain.Records} entity
 */
@Data
@JsonFilter("records")
public class RecordsDto implements Serializable {
    private String recordId;
    private String fileUrl;
    private String awardsTitle;
    private String history;
    private Date awardsDate;
    private String member;

    public RecordsDto(Records records, String name) {
        this.recordId = records.getRecordId();
        this.fileUrl = records.getFileUrl();
        this.awardsTitle = records.getAwardsTitle();
        this.history = records.getHistory();
        this.awardsDate = records.getAwardsDate();
        this.member = name;
    }
}