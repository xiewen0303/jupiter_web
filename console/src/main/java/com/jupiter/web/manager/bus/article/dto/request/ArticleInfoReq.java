package com.jupiter.web.manager.bus.article.dto.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ArticleInfoReq {

    private String id;
    private String title;
    private String content;
    private String categoryId;
    private Integer order;
    private MultipartFile headFile;
}
