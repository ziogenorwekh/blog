package com.portfolio.blog.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@NoArgsConstructor
public class UploadFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "FILE_ID", nullable = false)
    private Long id;

    @Column(name = "FILE_UUID", nullable = false, unique = true)
    private String fileId;
    @Column(unique = true, length = 100)
    private String filename;

    private String mimetype;

    private String fileUrl;

    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    private Date insertDate;


//    @ManyToOne
//    @JoinColumn(name = "POST_ID")
//    private Post post;

    @Builder
    public UploadFile(String fileId, String filename,String mimetype, String fileUrl) {
        this.fileId = fileId;
        this.filename = filename;
        this.mimetype = mimetype;
        this.fileUrl = fileUrl;
    }

    public static UploadFile create(String fileId, String filename,String mimetype,String fileUrl, Post post) {
        UploadFile uploadFile = UploadFile.builder()
                .fileId(fileId)
                .filename(filename)
                .mimetype(mimetype)
                .fileUrl(fileUrl)
                .build();
//        uploadFile.addPost(post);
        return uploadFile;
    }

//    public void addPost(Post post) {
//        this.post = post;
//        post.getFiles().add(this);
//    }

    public void delete() {

    }

}
